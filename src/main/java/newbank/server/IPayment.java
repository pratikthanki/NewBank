package newbank.server;

import java.util.HashMap;

public interface IPayment {

    //checks if customer exists
    Boolean checkCustomerExists(HashMap<String, Customer > map, String customer);

    //calculates the transaction
    Boolean calculateTransaction(Account from, Account to, Double amount);

    //check if amount is a numerical value
    Boolean isNumeric(String strNum);
}