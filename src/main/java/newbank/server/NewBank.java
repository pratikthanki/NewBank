package newbank.server;

import newbank.database.DatabaseClient;
import newbank.server.authentication.BasicAuthenticator;

import javax.xml.crypto.Data;
import java.net.Authenticator;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewBank {

    private static final NewBank bank = new NewBank();
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
    public synchronized String processRequest(CustomerID customer, String request) {
        if (customers.containsKey(customer.getKey())) {
            switch (parseString(request)[0]) {
                case "SHOWMYACCOUNTS":
                    return showMyAccounts(customer);
                case "NEWACCOUNT":
                    return createNewAccount(customer, request);
                case "MOVE":
                    return moveMoney(customer, request);
                case "CUSTOMERDETAIL":
                    return getCustomer(customer, request).getDetail();
                default:
                    return "FAIL";
            }
        }
        return "FAIL";
    }

    private Customer getCustomer(CustomerID customer, String request) {
        if (request == "CUSTOMERDETAIL") {
            return customers.get(customer.getKey());
        }
        return null;
    }

    private String createNewAccount(CustomerID customer, String request) {
        String[] requestAndDetails = request.split(" ");
        if (requestAndDetails.length == 2) {
            String newAccountName = requestAndDetails[1];
            customers.get(customer.getKey()).addAccount(new Account(newAccountName, 0.0, 1234));
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

    private String[] parseString(String inputString) {
        return inputString.split(" ");
    }

}
