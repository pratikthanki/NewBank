package newbank.server;

import newbank.database.DatabaseClient;
import newbank.server.authentication.BasicAuthenticator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                default:
                    return "FAIL";
            }
        }
        return "FAIL";
    }
    
    public synchronized String processRequest(CustomerID customer, Command command) {
    	Map<Parameter, String> properties = new HashMap<>();
    	return processRequest(customer, command, properties);
    }
        
    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customer, Command command, Map<Parameter, String> properties) {
        if (customers.containsKey(customer.getKey())) {
            switch (command) {
                case SHOWMYACCOUNTS:
                    return showMyAccounts(customer);
                case NEWACCOUNT:
                    return createNewAccount(customer, command, properties);
                case MOVE:
                    return moveMoney(customer, command, properties);
                case PAY:
                    return payMoney(customer, command, properties);
                case SHOWCUSTOMERDETAIL:
                    return getCustomer(customer, command, properties).getDetail();
                case GETCUSTOMEREMAIL:
                    return getCustomer(customer, command, properties).getEmail();
                case GETCUSTOMERADDRESS:
                    return getCustomer(customer, command, properties).getAddress();
                case GETCUSTOMERNAME:
                    return getCustomer(customer, command, properties).getName();
                case GETCUSTOMERDOB:
                    return String.format("%1$tb %1$te, %1$tY",getCustomer(customer, command, properties).getDob());
                case UPDATECUSTOMEREMAIL:
                    return updateCustomerEmail(customer, command, properties);
                case UPDATECUSTOMERADDRESS:
                    return updateCustomerAddress(customer, command, properties);
                case UPDATECUSTOMERDOB:
                    return updateCustomerDob(customer, command, properties);
                case UPDATECUSTOMERNAME:
                    return updateCustomerName(customer, command, properties);
                default:
                    return "FAIL: Internal Error.";
            }
        }
        return "FAIL";
    }

    private Customer getCustomer(CustomerID customer, Command command, Map<Parameter, String> properties) {
		if (customer.getKey() != null) {
			return customers.get(customer.getKey());
		}
		return null;
	}

    private String createNewAccount(CustomerID customerID, String request) {
        return "FAIL";
    }
    
	private String createNewAccount(CustomerID customer, Command command, Map<Parameter, String> properties) {
		String newAccountName = properties.get(Parameter.ACCOUNT_NAME);
        if (InputValidator.validateTextLength(newAccountName, 2, -1)) {
            customers.get(customer.getKey()).addAccount(new Account(newAccountName, 0.0, 1234));
            return "SUCCESS";
        }
        return "FAIL";
    }

    private String showMyAccounts(CustomerID customer) {
        return (customers.get(customer.getKey())).accountsToString();
    }

    private String moveMoney(CustomerID customerID, String request) {
    	return "FAIL";
    }
    
    private String moveMoney(CustomerID customer, Command command, Map<Parameter, String> properties) {
    	String amountx = properties.get(Parameter.AMOUNT);
    	String from_account = properties.get(Parameter.FROM_ACCOUNT);
    	String to_account = properties.get(Parameter.TO_ACCOUNT);
        Customer c = customers.get(customer.getKey()); 
        
        List<Account> accountsAssociatedToCustomer = c.getAccounts();
        Map<String, Account> mapOfAccountNamesToAccounts = new HashMap<>();
        for(Account a: accountsAssociatedToCustomer){
            mapOfAccountNamesToAccounts.put(a.getAccountName(), a);
        }
        if(properties.size() != 3){
            System.out.println("You have not provided all the required values to transfer money between your accounts. " +
                    "Please provide the request in the following format: MOVE <Amount> <FromAccount> <ToAccount>");
            return "FAIL";
        }

        if(!InputValidator.isNumeric(amountx)) return "FAIL";
        
        Double amount = Double.parseDouble(amountx);
        //Get the 'from' account
        Account from = mapOfAccountNamesToAccounts.get(from_account);
        if(from == null){
            return "FAIL: Provided 'from' account does not exist, please check your input and try again.\n";
        }

        //Get the 'to' account
        Account to = mapOfAccountNamesToAccounts.get(to_account);
        if(to == null){
            return "FAIL: Provided 'to' account does not exist, please check your input and try again.\n";
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
       return "FAIL";
    }
    
    //pay another person
    private String payMoney(CustomerID customer, Command command, Map<Parameter, String> properties) {
        
        //payer
        Customer payer = customers.get(customer.getKey());

        //check payee customer exists
        String payeeCustomerName = properties.get(Parameter.PAYEE_CUSTOMER_NAME);
        Customer payee = customers.get(payeeCustomerName);

        //check user input
        if (properties.size() != 3) {
            System.out.println("You have not provided all the required values to PAY money to another customer " +
                    "Please provide the request in the following format: PAY <CustomerName> <Amount> <AccountFrom>");
            return "FAIL";
        }

        //check if amount is a valid numerical value
        String strAmount = properties.get(Parameter.AMOUNT);
        if (!paymentHelper.isNumeric(strAmount)) {
            return "FAIL";
        }
        Double amount = Double.valueOf(strAmount);

        //Get the 'from' account
        Account from = payer.getHasMapForAllCustomerAccounts().get(properties.get(Parameter.FROM_ACCOUNT));

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
    
    private String updateCustomerEmail(CustomerID customer, Command command, Map<Parameter, String> properties) {
    	String newEmail = properties.get(Parameter.EMAIL);
         if (InputValidator.isEmailAddressValid(newEmail)) {
             customers.get(customer.getKey()).setEmail(newEmail); 
             return "SUCCESS";
         }
         return "FAIL";
	}
    
    private String updateCustomerAddress(CustomerID customer, Command command, Map<Parameter, String> properties) {
    	String newAddress = properties.get(Parameter.ADDRESS);
        
        if (InputValidator.validateTextLength(newAddress, 4, -1)) {
        	customers.get(customer.getKey()).setAddress(newAddress); 
            return "SUCCESS";
        }
            
        return "FAIL";
	}
    
    private String updateCustomerName(CustomerID customer, Command command, Map<Parameter, String> properties){
    	String newName = properties.get(Parameter.FIRSTNAME) + " " +  properties.get(Parameter.SURNAME);
   
    	if (InputValidator.validateTextLength(newName, 4, -1)) {
            customers.get(customer.getKey()).setName(newName); 
            return "SUCCESS";
        }
        return "FAIL";
	}
    
    private String updateCustomerDob(CustomerID customer, Command command, Map<Parameter, String> properties) {
    	String dob = properties.get(Parameter.DOB);
    	
        if (InputValidator.parseDate(dob, "d MM yyyy")==null) return "FAIL: Invalid date format for Date of Birth" ;
        
		try {
			SimpleDateFormat parser = new SimpleDateFormat("d MM yyyy");
			
            Date d;
			d = parser.parse(dob);
			customers.get(customer.getKey()).setDob(d); 
            return "SUCCESS";
            
		} catch (ParseException e) {
			return "Error: " + e.getMessage();
		}
        
	}

	public String registerNewCustomer(Map<Parameter,String> properties) {
		
		String firstname = properties.get(Parameter.FIRSTNAME);
		String surname = properties.get(Parameter.SURNAME);
		String newPassword = properties.get(Parameter.NEW_PASSWORD);
		String confirmPassword = properties.get(Parameter.CONFIRM_PASSWORD);
		String dob = properties.get(Parameter.DOB);
		String email = properties.get(Parameter.EMAIL);
		String address = properties.get(Parameter.ADDRESS);
		
		if (InputValidator.parseDate(dob, "d MM yyyy")==null) return "FAIL: Invalid Date of Birth.";
		if (!(newPassword.equals(confirmPassword))) return "FAIL: Password mismatch." + newPassword + " " + confirmPassword;
		if (!InputValidator.isEmailAddressValid(email)) return "FAIL: Invalid email address.";
		if (!InputValidator.validateTextLength(address,4, -1))return "FAIL: Invalid address length.";
		
		Customer customer = new Customer(firstname, surname);
		customer.setDob(InputValidator.parseDate(dob, "d MM yyyy"));
		customer.setEmail(email);
		customer.setAddress(address);

		customers.put(customer.getCustomerID(), customer);
		return "SUCCESS";
	}
	
    private String[] parseString(String inputString) {
        return inputString.split(" ");
    }
}