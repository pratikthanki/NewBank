package newbank.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class Customer {

    private final ArrayList<Account> accounts;
    private Date dob;
	private String email;
    private String name;
    private String address;
    private CustomerID customerID;

	public Customer() {
        accounts = new ArrayList<>();
    }

	//Constructor 2: Create customer while providing CustomerID
    public Customer(CustomerID customerID) {
    	this(); //Customer()
        this.customerID = customerID;
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
        account.setDefaultAccount(this);
    }

    //get number of default accounts
    public int getNumberOfAccounts() {
        return accounts.size();
    }

    //get a customer's default account
    public Account getDefaultAccount() {
        for (Account a: accounts){
            if (a.isDefaultAccount()){
                return a;
            }
        }
        return null;
    }

    //map each customer to customerName
    public HashMap<String, Account> getHasMapForAllCustomerAccounts(){
        // Create a HashMap
        HashMap < String, Account > map = new HashMap < > ();
        for (Account a: accounts) {
            map.put(a.getAccountName(), a);
        }
        return map;
    }

    //check if account exists
    public Boolean checkAccountExists(Account account) {
        try {

            HashMap < String, Account > map = getHasMapForAllCustomerAccounts();
            // Iterate over the HashMap
            for (Map.Entry < String, Account > entry: map.entrySet()) {
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
		if (setName(name) && setEmail(email) && setDob(dob) && setAddress(address) )
			return true;

		return false;
	}
}