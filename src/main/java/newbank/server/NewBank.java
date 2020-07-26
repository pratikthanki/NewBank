package newbank.server;

import newbank.database.DatabaseClient;
import newbank.server.authentication.BasicAuthenticator;

import java.io.PrintWriter;
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

    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        basicAuthenticator = new BasicAuthenticator(userName, password);

        return basicAuthenticator.ValidateLogin();
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(PrintWriter out, CustomerID customerID, String request) {
        if (customers.containsKey(customerID.getKey())) {
            switch (parseString(request)[0]) {
                case showMyAccounts:
                    return showMyAccounts(customerID);
                case newAccount:
                    return createNewAccount(customerID, request);
                case move:
                    return moveMoney(out, customerID, request);
                case pay:
                    return payMoney(out, customerID, request);
                case customerDetail:
                    return getCustomer(customerID, request).getDetail();
                default:
                    return Statuses.FAIL.toString();
            }
        }
        return Statuses.FAIL.toString();
    }

    private Customer getCustomer(CustomerID customer, String request) {
        if (request.equals(customerDetail)) {
            return customers.get(customer.getKey());
        }
        return null;
    }

    private String createNewAccount(CustomerID customerID, String request) {
        String[] requestAndDetails = request.split(" ");
        if (requestAndDetails.length == 2) {
            String newAccountName = requestAndDetails[1];
            customers.get(customerID.getKey()).addAccount(new Account(newAccountName, 0.0, 1234));
            return Statuses.SUCCESS.toString();
        }
        return Statuses.FAIL.toString();
    }

    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
    }

    private String moveMoney(PrintWriter out, CustomerID customerID, String request) {
        //User input
        String[] parsedInput = parseString(request);

        //customer
        Customer customer = customers.get(customerID.getKey());

        //check user input
        if (parsedInput.length != 4) {
            out.println(moveWrongSyntaxMessage);
            return Statuses.FAIL.toString();
        }

        //check if amount is a valid numerical value
        String strAmount = parsedInput[1];
        if (!paymentHelper.isNumeric(strAmount)) {
            return Statuses.FAIL.toString();
        }
        Double amount = Double.valueOf(strAmount);

        //Get the 'from' account
        Account from = customer.getHasMapForAllCustomerAccounts().get(parsedInput[2]);

        //Get the 'to' account
        Account to = customer.getHasMapForAllCustomerAccounts().get(parsedInput[3]);

        //check if accounts exist and if the 'payerAccount' account has sufficient balance for the money move
        if (customer.checkAccountExists(from) || customer.checkAccountExists(to)) {
            return Statuses.FAIL.toString();
        }

        //Calculate transaction
        if (!paymentHelper.calculateTransaction(from, to, amount)) {
            return Statuses.FAIL.toString();
        } else {
            return Statuses.SUCCESS.toString();
        }
    }

    //pay another person
    private String payMoney(PrintWriter out, CustomerID customerID, String request) {
        //User input
        String[] parsedInput = parseString(request);

        //payer
        Customer payer = customers.get(customerID.getKey());

        //check payee customer exists
        String payeeCustomerName = parsedInput[1];
        Customer payee = customers.get(payeeCustomerName);

        //check user input
        if (parsedInput.length != 4) {
            out.println(payWrongSyntaxMessage);
            return Statuses.FAIL.toString();
        }

        //check if amount is a valid numerical value
        String strAmount = parsedInput[2];
        if (!paymentHelper.isNumeric(strAmount)) {
            return Statuses.FAIL.toString();
        }
        Double amount = Double.valueOf(strAmount);

        //Get the 'from' account
        Account from = payer.getHasMapForAllCustomerAccounts().get(parsedInput[3]);

        //Check if customer exists and Get the 'to' account
        if (!paymentHelper.checkCustomerExists(customers, payeeCustomerName)) {
            return Statuses.FAIL.toString();
        }
        Account to = payee.getDefaultAccount();

        //check if accounts exist and if the 'payerAccount' account has sufficient balance for the money move
        if (payer.checkAccountExists(from) || payee.checkAccountExists(to)) {
            return Statuses.FAIL.toString();
        }

        //Calculate transaction
        if (!paymentHelper.calculateTransaction(from, to, amount)) {
            return Statuses.FAIL.toString();
        } else {
            return Statuses.SUCCESS.toString();
        }

    }

    private String[] parseString(String inputString) {
        return inputString.split(" ");
    }
}