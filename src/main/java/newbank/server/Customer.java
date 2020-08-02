package newbank.server;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private int id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "account_ids")
	private List<Account> accounts;

	@Column(name = "date_of_birth")
	private Date dob;

	@Column(name = "email")
	private String email;

	@Column(name = "name")
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "customer_id", unique = true)
	private String customerID;

	@Column(name = "password")
	private String password;

	public Customer() {
		this.accounts = new ArrayList<>();
	}

	//Constructor 2: Create customer while providing CustomerID
	public Customer(CustomerID customerID) {
		this.accounts = new ArrayList<>();
		this.customerID = customerID.getKey();
	}

	public CustomerID getCustomerID() {
		return new CustomerID(customerID);
	}

	public void setCustomerID(CustomerID customerID){
		this.customerID = customerID.getKey();
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	//Important for Hibernate
	public void setAccounts(List<Account> accounts){
		this.accounts = accounts;
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
		account.setDefaultAccount(this);
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
		for (Account a : getAccounts()) {
			map.put(a.getAccountName(), a);
		}
		return map;
	}

	//check if account exists
	public Boolean checkAccountExists(Account account) {
		try {

			HashMap<String, Account> map = getHasMapForAllCustomerAccounts();
			// Iterate over the HashMap
			for (Map.Entry<String, Account> entry : map.entrySet()) {
				// Get the entry at this iteration and check if this key is the required key
				if (account.toString().equals(entry.getKey())) {
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
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
		if (isValidPassword(password)) this.password = password;

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

	@Override
	public String toString() {
		return "Customer{" +
				"id=" + id +
//				", accounts=" + accounts +
				", dob=" + dob +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				", customerID='" + customerID + '\'' +
				", password='" + password + '\'' +
				'}';
	}
}
