package newbank.server;

import java.util.HashMap;

public interface PaymentInterface {
    Boolean checkCustomerExists(HashMap<String, Customer > map, String customer);
    Boolean checkAccountExists(Account account, Customer customer);
    Boolean calculateTransaction(Account from, Account to, Double amount);
    Boolean isNumeric(String strNum);
}
