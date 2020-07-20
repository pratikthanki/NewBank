package newbank.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentHelper implements PaymentInterface {

    @Override
    //check if account exists
    public Boolean checkAccountExists(Account account, Customer customer) {
        try {

            HashMap < String, Account > map = getHasMapForAllCustomerAccounts(customer);
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

    @Override
    //check if customer exists
    public Boolean checkCustomerExists(HashMap <String, Customer > map, String customer) {
        try {
            // Iterate over the HashMap
            for (Map.Entry < String, Customer > entry: map.entrySet()) {
                // Get the entry at this iteration and check if this key is the required key
                if (customer.equals(entry.getKey())) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    //check if amount is a numerical value
    public Boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean calculateTransaction(Account from, Account to, Double amount) {
        //check for sufficient balance
        if (from == null) {
            return false;
        }
        if (from.getBalance() < amount) {
            System.out.println("This action is invalid, as this account does not have a sufficient balance.");
            return false;
        //execute transaction
        } else {
            from.withdrawMoney(amount);
            to.addMoney(amount);
            return true;
        }
    }

    //map each customer to customerName
    public HashMap<String, Account> getHasMapForAllCustomerAccounts(Customer customer){
        // Create a HashMap
        HashMap < String, Account > map = new HashMap < > ();
        for (Account a: getAllAccountsListForACustomer(customer)) {
            map.put(a.getAccountName(), a);
        }
        return map;
    }

    //get a list of all customer accounts
    public List <Account> getAllAccountsListForACustomer(Customer customer){
        return customer.getAccounts();
    }

    //get a customer's default account
    public Account getDefaultAccount(Customer customer) {
        for (Account a: getAllAccountsListForACustomer(customer)){
            if (a.isDefaultAccount()){
                return a;
            }
        }
        return null;
    }
}