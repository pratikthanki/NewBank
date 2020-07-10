package newbank.server;

import newbank.server.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewBank {
	
	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	
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
		if(customers.containsKey(userName)) {
			return new CustomerID(userName);
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
				case "SHOWMYACCOUNTS" :
					return showMyAccounts(customer);
				case "MOVE":
					return moveMoney(customer, request);
				default : return "FAIL";
			}
		}
		return "FAIL";
	}
	
	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	private String moveMoney(CustomerID customerID, String request) {
		System.out.println("Hi");
		String[] parsedInput = parseString(request);
		Customer customer = customers.get(customerID.getKey());
		List<Account> accountsAssociatedToCustomer = customer.getAccounts();
		Map<String, Account> mapOfAccountNamesToAccounts = new HashMap<>();
		for(Account a: accountsAssociatedToCustomer){
			mapOfAccountNamesToAccounts.put(a.getAccountName(), a);
		}
		if(parsedInput.length != 3){
			System.out.println("You have not provided all the required values to transfer money between your accounts. " +
					"Please provide the request in the following format: MOVE <Amount> <FromAccount> <ToAccount>");
			return "FAIL";
		}

		//Get amount
		Double amount = Double.valueOf(parsedInput[0]);

		//Get the 'from' account
		Account from = null;
		if(mapOfAccountNamesToAccounts.get(parsedInput[1]) != null){
			from = mapOfAccountNamesToAccounts.get(parsedInput[1]);
		} else {
			System.out.println("Provided 'from' account does not exist, please check your input and try again.");
			return "FAIL";
		}


		//Get the 'to' account
		Account to = null;
		if(mapOfAccountNamesToAccounts.get(parsedInput[2]) != null){
			to = mapOfAccountNamesToAccounts.get(parsedInput[2]);
		} else {
			System.out.println("Provided 'to' account does not exist, please check your input and try again.");
			return "FAIL";
		}

		if(from.getOpeningBalance() < amount){
			System.out.println("This action is invalid, as this account does not have a sufficient balance.");
			return "FAIL";
		} else {
			from.setOpeningBalance(from.getOpeningBalance() - amount);
			to.setOpeningBalance(to.getOpeningBalance() + amount);
			System.out.println("From Account:" + from.toString());
			System.out.println("To Account:" + to.toString());
			return "SUCCESS";
		}
	}

	private String[] parseString(String inputString){
		return inputString.split(" ");
	}
}
