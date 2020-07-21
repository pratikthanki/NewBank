package newbank.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Customer {

    private final ArrayList<Account> accounts;

    public Customer() {
        accounts = new ArrayList<>();
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
}
