package newbank.server;

import java.util.HashMap;

public class NewBank {

    private static final NewBank bank = new NewBank();
    private HashMap<String, Customer> customers;

    private NewBank() {
        customers = new HashMap<>();
        addTestData();
    }

    private void addTestData() {
        Customer bhagy = new Customer();
        bhagy.addAccount(new Account("Main", 1000.0));
        customers.put("Bhagy", bhagy);

        Customer christina = new Customer();
        christina.addAccount(new Account("Savings", 1500.0));
        customers.put("Christina", christina);

        Customer john = new Customer();
        john.addAccount(new Account("Checking", 250.0));
        customers.put("John", john);
    }

    public static NewBank getBank() {
        return bank;
    }

    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        if (customers.containsKey(userName)) {
            return new CustomerID(userName);
        }
        return null;
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customer, String request) {
        if (customers.containsKey(customer.getKey())) {
            switch (request.split(" ")[0]) {
                case "SHOWMYACCOUNTS":
                    return showMyAccounts(customer);
                case "NEWACCOUNT":
                    return handleCreateNewAccount(customer, request);
                default:
                    return "FAIL";
            }
        }
        return "FAIL";
    }

    private String handleCreateNewAccount(CustomerID customer, String request) {
        String[] requestAndDetails = request.split(" ");
        if (requestAndDetails.length == 2) {
            String newAccountName = requestAndDetails[1];
            return createNewAcountAndDisplay(customer, newAccountName);
        }
        return "FAIL";
    }

    private String createNewAcountAndDisplay(CustomerID customerID, String newAccountName) {
        customers.get(customerID.getKey()).addAccount(new Account(newAccountName, 0.0));
        return "SUCCESS";
    }

    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
    }

}
