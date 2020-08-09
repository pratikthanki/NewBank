package newbank.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class Customer {

	private final ArrayList<Account> accounts;
	private Date dob;
	private String email;
	private String name;
	private String address;
	private CustomerID customerID;
	private String password;

	public Customer() {
		accounts = new ArrayList<>();
	}

	//Constructor 2: Create customer while providing CustomerID
	public Customer(CustomerID customerID) {
		this(); //Customer()
		this.customerID = customerID;
	}
	
	//Constructor 3
	public Customer(String firstname, String surname) {
    	this(); 
        this.customerID = new CustomerID(firstname);
        this.name = firstname + " " + surname;
	 }

	public CustomerID getCustomerID() {
		return customerID;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public String accountsToString() {
		String s = "";
		for (Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public void setDefaultAccount(Account account){
		account.setDefaultAccountStatus();
	}

	//get number of default accounts
	public int getNumberOfAccounts() {
		return accounts.size();
	}

	//get a customer's default account
	public Account getDefaultAccount() {
		for (Account a : accounts) {
			if (a.isDefaultAccount()) {
				return a;
			}
		}
		return null;
	}

	//map each customer to customerName
	public HashMap<String, Account> getHasMapForAllCustomerAccounts() {
		// Create a HashMap
		HashMap<String, Account> map = new HashMap<>();
		for (Account a : accounts) {
			map.put(a.getAccountName(), a);
		}
		return map;
	}

	public Date getDob() {
		return this.dob;
	}

	public boolean setDob(Date dob) {
		this.dob = dob;
		return true;
	}

	public String getEmail() {
		return this.email;
	}

	public boolean setEmail(String email) {
		this.email = email;
		return true;
	}

	public String getName() {
		return this.name;
	}

	public boolean setName(String name) {
		this.name = name;
		return true;
	}

	public String getAddress() {
		return this.address;
	}

	public boolean setAddress(String address) {
		this.address = address;
		return true;
	}

	public String getDetail() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("Customer Name: \t" + getName() + "\n");
		buffer.append("Date of Birth: \t" + getDob() + "\n");
		buffer.append("Email Address: \t" + getEmail() + "\n");
		buffer.append("Address: \t" + getAddress() + "\n");

		return buffer.toString();
	}

	public boolean updateDetail(String name, Date dob, String email, String address) {
		return setName(name) && setEmail(email) && setDob(dob) && setAddress(address);
	}

	public boolean setPassword(String password) {
		if (isValidPassword(password)) {
			this.password = password;
			return true;
		}

		return false;
	}

	public String getPassword() {
		return this.password;
	}

	private boolean isValidPassword(String password) {
		int minimumPasswordLength = 8;
		if (password.length() < minimumPasswordLength) return false;

		int charCount = 0;
		int numCount = 0;
		for (int i = 0; i < password.length(); i++) {
			char ch = password.charAt(i);

			if (isInt(ch)) numCount++;
			else if (isLetter(ch)) charCount++;
			else return false;
		}
		return (charCount >= 2 && numCount >= 2);
	}

	private static boolean isLetter(char ch) {
		ch = Character.toUpperCase(ch);
		return (ch >= 'A' && ch <= 'Z');
	}

	private static boolean isInt(char ch) {
		return (ch >= '0' && ch <= '9');
	}
}
