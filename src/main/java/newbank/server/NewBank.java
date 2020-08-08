package newbank.server;

import newbank.database.DatabaseClient;
import newbank.database.HibernateDatabaseClient;
import newbank.database.HibernateUtility;
import newbank.server.authentication.BasicAuthenticator;

import java.util.HashMap;

import static newbank.database.static_data.NewBankData.*;

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
                case showMyAccounts:
                    return showMyAccounts(customerID);
                case newAccount:
                    return createNewAccount(customerID, request);
                case move:
                    return moveMoney(customerID, request);
                case pay:
                    return payMoney(customerID, request);
                case customerDetail:
                    return getCustomer(customerID, request).getDetail();
                default:
                    return Status.FAIL.toString();
            }
        }
        return Status.FAIL.toString();
    }

    private Customer getCustomer(CustomerID customer, String request) {
        if (request.equals(customerDetail)) {
            return databaseClient.getCustomerById(customer);
        }
        return null;
    }

    private String createNewAccount(CustomerID customerID, String request) {
        String[] requestAndDetails = request.split(emptyString);
        if (requestAndDetails.length == 2) {
            String newAccountName = requestAndDetails[1];
            databaseClient.addAccount(customerID, new Account(newAccountName, 0.0, 1234));
            return Status.SUCCESS.toString();
        }
        return Status.FAIL.toString();
    }

    private String showMyAccounts(CustomerID customer) {
        return databaseClient.getAccountsAsString(customer);
    }

    private String moveMoney(CustomerID customerID, String request) {
        //User input
        String[] parsedInput = parseString(request);

        if (parsedInput.length != 4) return Status.FAIL.toString();

        //customer
        Customer customer = databaseClient.getCustomerById(customerID);

        //amount
        String userInputAmount = parsedInput[1];
        if (!validUserAmount(userInputAmount)) return Status.FAIL.toString();
        Double amount = Double.parseDouble(userInputAmount);

        HashMap<String, Account> customerAccounts = customer.getHasMapForAllCustomerAccounts();

        //Get the 'from' account
        Account from = customerAccounts.get(parsedInput[2]);

        //Get the 'to' account
        Account to = customerAccounts.get(parsedInput[3]);

        if (from == null || to == null) return Status.FAIL.toString();

        //Calculate transaction
        paymentHelper.calculateTransaction(from, to, amount);
        return Status.SUCCESS.toString();
    }

    //pay another person
    private String payMoney(CustomerID customerID, String request) {
        //User input
        String[] parsedInput = parseString(request);
        if (parsedInput.length != 4) return Status.FAIL.toString();

        //payer
        Customer payer = databaseClient.getCustomerById(customerID);
        HashMap<String, Account> payerAccounts = payer.getHasMapForAllCustomerAccounts();

        //payee
        String payeeCustomerName = parsedInput[1];
        Customer payee = databaseClient.getCustomerById(payeeCustomerName);
        if (payee == null) return Status.FAIL.toString();

        HashMap<String, Account> payeeAccounts = payee.getHasMapForAllCustomerAccounts();
        if (payeeAccounts.size() == 0) return Status.FAIL.toString();

        //check if amount is a valid numerical value
        String userInputAmount = parsedInput[2];

        if (!validUserAmount(userInputAmount)) return Status.FAIL.toString();
        Double amount = Double.parseDouble(userInputAmount);

        //Get the 'from' account
        Account from = payerAccounts.get(parsedInput[3]);

        //Get payee's default account
        Account to = payee.getDefaultAccount();

        if (from == null || to == null) return Status.FAIL.toString();

        //Calculate transaction
        paymentHelper.calculateTransaction(from, to, amount);
        databaseClient.updateCustomer(payer);
        databaseClient.updateCustomer(payee);
        return Status.SUCCESS.toString();
    }

    public boolean validUserAmount(final String s) {
        try {
            final double move = Double.parseDouble(s);
            return true;
        } catch (final NumberFormatException e) {
            System.out.printf("Error: invalid input \"%s\", please try again.\n", s);
        }
        return false;
    }
    private String[] parseString(String inputString) {
        return inputString.split(emptyString);
    }

    public Customer getCustomerById(String customerID) {
        return databaseClient.getCustomerById(customerID);
    }
}