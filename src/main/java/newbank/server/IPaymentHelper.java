package newbank.server;

import java.util.HashMap;
import java.util.Map;

public class IPaymentHelper implements IPayment {

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







}