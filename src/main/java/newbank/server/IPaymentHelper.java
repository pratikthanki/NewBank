package newbank.server;

import java.io.PrintWriter;

public class IPaymentHelper implements IPayment {

    @Override
    //check if amount is a numerical value
    public Boolean isNumeric(PrintWriter out, String strNum) {
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
    public void calculateTransaction(Account from, Account to, Double amount) {
            from.withdrawMoney(amount);
            to.addMoney(amount);
    }


    public Boolean checkAccountHasSufficientBalance(PrintWriter out, Account from, Double amount) {
        //execute transaction
        return !(from.getBalance() < amount);
    }
}