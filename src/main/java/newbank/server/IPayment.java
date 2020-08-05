package newbank.server;

import java.io.PrintWriter;
import java.util.HashMap;

public interface IPayment {

    //calculates the transaction
    void calculateTransaction(Account from, Account to, Double amount);

    //check if amount is a numerical value
    Boolean isNumeric(PrintWriter out, String strNum);

    //check account has sufficient balance
    Boolean checkAccountHasSufficientBalance(PrintWriter out, Account from, Double amount);
}