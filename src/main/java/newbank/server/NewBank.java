package newbank.server;

import newbank.database.DatabaseClient;
import newbank.database.HibernateDatabaseClient;
import newbank.database.HibernateUtility;
import newbank.database.MapDatabaseClient;
import newbank.server.authentication.BasicAuthenticator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewBank {

    private static NewBank bank;
    private static final IPaymentHelper paymentHelper = new IPaymentHelper();
    private final DatabaseClient databaseClient;
    private BasicAuthenticator basicAuthenticator;

    private NewBank(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public static NewBank getBank() {
        if(bank == null){
            bank = new NewBank(new HibernateDatabaseClient(HibernateUtility.development()));
        }
        return bank;
    }

    public static void init(DatabaseClient databaseClient){
        if(bank == null){
            bank = new NewBank(databaseClient);
        }
    }

    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        basicAuthenticator = new BasicAuthenticator(userName, password);

        return basicAuthenticator.ValidateLogin();
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customerID, String request) {
        if (databaseClient.hasCustomer(customerID.getKey())) {
            switch (parseString(request)[0]) {
                case "SHOWMYACCOUNTS":
                    return showMyAccounts(customerID);
                case "NEWACCOUNT":
                    return createNewAccount(customerID, request);
                case "MOVE":
                    return moveMoney(customerID, request);
                case "PAY":
                    return payMoney(customerID, request);
                case "CUSTOMERDETAIL":
                    return getCustomer(customerID, request).getDetail();
                default:
                    return "FAIL";
            }
        }
        return "FAIL";
    }

    private Customer getCustomer(CustomerID customer, String request) {
        if (request.equals("CUSTOMERDETAIL")) {
            return databaseClient.getCustomerById(customer);
        }
        return null;
    }

    private String createNewAccount(CustomerID customerID, String request) {
        String[] requestAndDetails = request.split(" ");
        if (requestAndDetails.length == 2) {
            String newAccountName = requestAndDetails[1];
            databaseClient.addAccount(customerID, new Account(newAccountName, 0.0, 1234));
            return "SUCCESS";
        }
        return "FAIL";
    }

    private String showMyAccounts(CustomerID customer) {
        return databaseClient.getAccountsAsString(customer);
    }

    private String moveMoney(CustomerID customerID, String request) {
        String[] parsedInput = parseString(request);
        Customer customer = databaseClient.getCustomerById(customerID);
        List<Account> accountsAssociatedToCustomer = customer.getAccounts();
        Map<String, Account> mapOfAccountNamesToAccounts = new HashMap<>();
        for (Account a : accountsAssociatedToCustomer) {
            mapOfAccountNamesToAccounts.put(a.getAccountName(), a);
        }
        if (parsedInput.length != 4) {
            System.out.println("You have not provided all the required values to transfer money between your accounts. " +
                    "Please provide the request in the following format: MOVE <Amount> <FromAccount> <ToAccount>");
            return "FAIL";
        }

        //Get amount
        Double amount = Double.valueOf(parsedInput[1]);

        //Get the 'from' account
        Account from = mapOfAccountNamesToAccounts.get(parsedInput[2]);
        if (from == null) {
            System.out.println("Provided 'from' account does not exist, please check your input and try again.");
            return "FAIL";
        }

        //Get the 'to' account
        Account to = mapOfAccountNamesToAccounts.get(parsedInput[3]);
        if (to == null) {
            System.out.println("Provided 'to' account does not exist, please check your input and try again.");
            return "FAIL";
        }

        //Check if the 'from' account has sufficient balance for the money move
        if (from.getBalance() < amount) {
            System.out.println("This action is invalid, as this account does not have a sufficient balance.");
            return "FAIL";
        } else {
            from.withdrawMoney(amount);
            to.addMoney(amount);
            System.out.println("From Account:" + from.toString());
            System.out.println("To Account:" + to.toString());
            return "SUCCESS";
        }
    }

    //pay another person
    private String payMoney(CustomerID customerID, String request) {
        String[] parsedInput = parseString(request);

        //payer
        Customer payer = databaseClient.getCustomerById(customerID);

        //check payee customer exists
        String payeeCustomerName = parsedInput[1];
        Customer payee = databaseClient.getCustomerById(payeeCustomerName);

        //check user input
        if (parsedInput.length != 4) {
            System.out.println("You have not provided all the required values to PAY money to another customer " +
                    "Please provide the request in the following format: PAY <CustomerName> <Amount> <AccountFrom>");
            return "FAIL";
        }

        //check if amount is a valid numerical value
        String strAmount = parsedInput[2];
        if (!paymentHelper.isNumeric(strAmount)) {
            return "FAIL";
        }
        Double amount = Double.valueOf(strAmount);

        //Get the 'from' account
        Account from = payer.getHasMapForAllCustomerAccounts().get(parsedInput[3]);

        //Get the 'to' account
        if (!databaseClient.hasCustomer(payeeCustomerName)) {
            return "FAIL";
        }
        Account to = payee.getDefaultAccount();

        //check if accounts exist and if the 'payerAccount' account has sufficient balance for the money move
        if (payer.checkAccountExists(from) || payee.checkAccountExists(to)) {
            return "FAIL";
        }

        //PAY Name Amount FromAccount
        if (!paymentHelper.calculateTransaction(from, to, amount)) {
            return "FAIL";
        } else {
            databaseClient.updateCustomer(payer);
            databaseClient.updateCustomer(payee);
            return "SUCCESS";
        }

    }

    private String[] parseString(String inputString) {
        return inputString.split(" ");
    }
}