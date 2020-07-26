package newbank.server;

import newbank.database.DatabaseClient;
import newbank.server.authentication.BasicAuthenticator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewBank {

    private static final NewBank bank = new NewBank();
    private static final IPaymentHelper paymentHelper = new IPaymentHelper();
    private DatabaseClient databaseClient = new DatabaseClient();
    private HashMap<String, Customer> customers;
    private BasicAuthenticator basicAuthenticator;

    private NewBank() {
        customers = databaseClient.getCustomers();
    }

    public static NewBank getBank() {
        return bank;
    }

    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        basicAuthenticator = new BasicAuthenticator(userName, password);

        return basicAuthenticator.ValidateLogin();
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customerID, String request) {
        if (customers.containsKey(customerID.getKey())) {
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
            return customers.get(customer.getKey());
        }
        return null;
    }

    private String createNewAccount(CustomerID customerID, String request) {
        String[] requestAndDetails = request.split(" ");
        if (requestAndDetails.length == 2) {
            String newAccountName = requestAndDetails[1];
            customers.get(customerID.getKey()).addAccount(new Account(newAccountName, 0.0, 1234));
            return "SUCCESS";
        }
        return "FAIL";
    }

    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
    }

    private String moveMoney(CustomerID customerID, String request) {
        String[] parsedInput = parseString(request);
        Customer customer = customers.get(customerID.getKey());
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
        Customer payer = customers.get(customerID.getKey());

        //check payee customer exists
        String payeeCustomerName = parsedInput[1];
        Customer payee = customers.get(payeeCustomerName);

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
        if (!paymentHelper.checkCustomerExists(customers, payeeCustomerName)) {
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
            return "SUCCESS";
        }

    }

    private String[] parseString(String inputString) {
        return inputString.split(" ");
    }
}