package newbank.server;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewBank {

    private static final NewBank bank = new NewBank();
    private static final IPaymentHelper paymentHelper = new IPaymentHelper();
    private final HashMap<String,Customer> customers;

    private NewBank() {
        customers = new HashMap<>();
        addTestData();
    }

    private void addTestData() {
        Customer bhagy = new Customer();
        bhagy.addAccount(new Account("Main", 1000.0));
        bhagy.updateDetail("Baby Hagy", new GregorianCalendar(1982, Calendar.DECEMBER, 20).getTime(), "bhagy@bath.ac.uk", "Bath, London");
        customers.put("Bhagy", bhagy);

        Customer christina = new Customer();
        christina.addAccount(new Account("Savings", 1500.0));
        christina.updateDetail("Christina Aguilera", new GregorianCalendar(1985, Calendar.JANUARY, 11).getTime(), "christina.aguilera@celebrity.com", "Houston, USA");
        customers.put("Christina", christina);

        Customer john = new Customer();
        john.addAccount(new Account("Checking", 250.0));
        john.addAccount(new Account("Savings", 50.0));
        john.updateDetail("John Doe", new Date(), "john.doe@newbank.com", "Lagos, Nigeria");
        customers.put("John", john);
    }

    public static NewBank getBank() {
        return bank;
    }

    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        if(customers.containsKey(userName)) {
            return new CustomerID(userName);
        }
        return null;
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
                case "PAY":
                    return payMoney(customer, request);
                case "CUSTOMERDETAIL":
                    return getCustomer(customer, request).getDetail();
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

	private String createNewAccount(CustomerID customer, String request) {
        String[] requestAndDetails = request.split(" ");
        if (requestAndDetails.length == 2) {
            String newAccountName = requestAndDetails[1];
            customers.get(customer.getKey()).addAccount(new Account(newAccountName, 0.0));
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
        for(Account a: accountsAssociatedToCustomer){
            mapOfAccountNamesToAccounts.put(a.getAccountName(), a);
        }
        if(parsedInput.length != 4){
            System.out.println("You have not provided all the required values to transfer money between your accounts. " +
                    "Please provide the request in the following format: MOVE <Amount> <FromAccount> <ToAccount>");
            return "FAIL";
        }

        //Get amount
        Double amount = Double.valueOf(parsedInput[1]);

        //Get the 'from' account
        Account from = mapOfAccountNamesToAccounts.get(parsedInput[2]);
        if(from == null){
            System.out.println("Provided 'from' account does not exist, please check your input and try again.");
            return "FAIL";
        }

        //Get the 'to' account
        Account to = mapOfAccountNamesToAccounts.get(parsedInput[3]);
        if(to == null){
            System.out.println("Provided 'to' account does not exist, please check your input and try again.");
            return "FAIL";
        }

        //Check if the 'from' account has sufficient balance for the money move
        if(from.getBalance() < amount){
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
    private String payMoney(CustomerID customerID, String request){
        String[] parsedInput = parseString(request);

        //payer
        Customer payer = customers.get(customerID.getKey());

        //check payee customer exists
        String payeeCustomerName = parsedInput[1];
        Customer payee = customers.get(payeeCustomerName);

        //check user input
        if(parsedInput.length != 4){
            System.out.println("You have not provided all the required values to PAY money to another customer " +
                    "Please provide the request in the following format: PAY <CustomerName> <Amount> <AccountFrom>");
            return "FAIL";
        }

        //check if amount is a valid numerical value
        String strAmount = parsedInput[2];
        if (!paymentHelper.isNumeric(strAmount)){
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
        if (payer.checkAccountExists(from) || payee.checkAccountExists(to)){
            return "FAIL";
        }

        //PAY Name Amount FromAccount
        if (!paymentHelper.calculateTransaction(from, to, amount)){
            return "FAIL";
        }
        else{return "SUCCESS";}
    }

    private String[] parseString(String inputString){
        return inputString.split(" ");
    }

}
