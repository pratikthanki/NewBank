package newbank.server;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Customer {

    private ArrayList<Account> accounts;
    private Date dob;
	private String email;
    private String name;
    private String address;
    private CustomerID customerID;

	public Customer() {
        accounts = new ArrayList<>();
    }
    
    public Customer(String firstname, String surname) {
    	this(); 
        this.customerID = new CustomerID(firstname);
        this.name = firstname + " " + surname;
    }
    
    public String getCustomerID() {
		return customerID.getKey();
	}

    public ArrayList<Account> getAccounts(){
        return accounts;
    }

    public String accountsToString() {
        String s = "";
        for(Account a : accounts) {
            s += a.toString();
        }
        return s;
    }

    public void addAccount(Account account) {
        accounts.add(account);
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
		buffer.append("Date of Birth: \t" + DateFormat.getDateInstance().format(getDob()) + "\n");
		buffer.append("Email Address: \t" + getEmail() + "\n");
		buffer.append("Address: \t" + getAddress() + "\n");
		
		return buffer.toString();
	}
}
