package newbank.server;

import java.io.PrintWriter;
import java.util.HashMap;
import static newbank.database.static_data.NewBankData.*;

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

    public void processMenuSelection(PrintWriter out) {
        // Print values
        int count = 0;
        for (String key : getMainMenu().values()) {
            count++;
            out.println(count + ".\t" + key);
        }
        out.println(nine + quit);
    }

    private HashMap<String, String> getMainMenu() {
        //Menu options and requests
        HashMap<String, String> menuOptions = new HashMap<>();
        menuOptions.put(one, showMyAccounts);
        menuOptions.put(two, newAccount);
        menuOptions.put(three, move);
        menuOptions.put(four, pay);
        menuOptions.put(five, customerDetail);
        return menuOptions;
    }
}