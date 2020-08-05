package newbank.server;

import newbank.database.DatabaseClient;
import newbank.server.authentication.BasicAuthenticator;

import java.util.ArrayList;
import java.util.HashMap;

import static newbank.database.static_data.NewBankData.*;

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

    public HashMap<String, Customer> getCustomers() {
        return customers;
    }

    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        basicAuthenticator = new BasicAuthenticator(userName, password);

        return basicAuthenticator.ValidateLogin();
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customerID, String request) {
        if (customers.containsKey(customerID.getKey())) {
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
            return customers.get(customer.getKey());
        }
        return null;
    }

    private String createNewAccount(CustomerID customerID, String request) {
        String[] requestAndDetails = request.split(emptyString);
        if (requestAndDetails.length == 2) {
            String newAccountName = requestAndDetails[1];
            customers.get(customerID.getKey()).addAccount(new Account(newAccountName, 0.0, 1234));
            return Status.SUCCESS.toString();
        }
        return Status.FAIL.toString();
    }

    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
    }

    private String moveMoney(CustomerID customerID, String request) {
        //User input
        String[] parsedInput = parseString(request);
        if (parsedInput.length != 4) return Status.FAIL.toString();

        //customer
        Customer customer = customers.get(customerID.getKey());

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
        Customer payer = customers.get(customerID.getKey());
        HashMap<String, Account> payerAccounts = payer.getHasMapForAllCustomerAccounts();

        //payee
        String payeeCustomerName = parsedInput[1];
        Customer payee;
        payee = customers.get(payeeCustomerName);
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
}