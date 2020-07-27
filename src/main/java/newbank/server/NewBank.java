package newbank.server;

import newbank.database.DatabaseClient;
import newbank.server.authentication.BasicAuthenticator;
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

        //customer
        Customer customer = customers.get(customerID.getKey());

        //amount
        String strAmount = parsedInput[1];
        Double amount = Double.valueOf(strAmount);

        //Get the 'from' account
        Account from = customer.getHasMapForAllCustomerAccounts().get(parsedInput[2]);

        //Get the 'to' account
        Account to = customer.getHasMapForAllCustomerAccounts().get(parsedInput[3]);

        //Calculate transaction
        paymentHelper.calculateTransaction(from, to, amount);
        return Status.SUCCESS.toString();
    }

    //pay another person
    private String payMoney(CustomerID customerID, String request) {
        //User input
        String[] parsedInput = parseString(request);

        //payer
        Customer payer = customers.get(customerID.getKey());

        //payee
        String payeeCustomerName = parsedInput[1];
        Customer payee;
        payee = customers.get(payeeCustomerName);

        //check if amount is a valid numerical value
        String strAmount = parsedInput[2];
        Double amount = Double.valueOf(strAmount);

        //Get the 'from' account
        Account from = payer.getHasMapForAllCustomerAccounts().get(parsedInput[3]);

        //Get payee's default account
        Account to = payee.getDefaultAccount();

        //Calculate transaction
        paymentHelper.calculateTransaction(from, to, amount);
        return Status.SUCCESS.toString();
    }

    private String[] parseString(String inputString) {
        return inputString.split(emptyString);
    }
}