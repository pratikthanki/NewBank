package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import static newbank.database.static_data.NewBankData.*;
import static newbank.server.MainMenu.*;

public class InputHandler {

    IPaymentHelper iPaymentHelper = new IPaymentHelper();

    /*
        User actions
     */

    public void showMyAccounts(PrintWriter out, CustomerID customerID, NewBank bank) {
        out.println(requestFrom + customerID.getKey());
        String response = bank.processRequest(customerID, ShowMyAccounts.getMenuOption());
        out.println(response);
    }

    public void newAccount(BufferedReader in , PrintWriter out, CustomerID customerID, NewBank bank) {
        String newAccountName;
        out.println(requestFrom + customerID.getKey());
        newAccountName = verifyNewAccountName(out, in );
        String response2 = bank.processRequest(customerID, NewAccount.getMenuOption() + emptyString + newAccountName);
        out.println(response2);
    }

    public void moveMoneyBetweenAccounts(BufferedReader in , PrintWriter out, CustomerID customerID, NewBank bank) {
        String amount;
        String from;
        String to;
        out.println(requestFrom + customerID.getKey());

        amount = verifyAmount(out, in );
        Double dAmount = Double.valueOf(amount);
        from = verifyFromAccount(out, in , customerID, bank);
        to = verifyToAccount(out, in , customerID, bank);

        if (!verifyPayerSufficientBalance(out, customerID, bank, dAmount, from)) {
            out.println(accountLabel + from + insufficientBalanceMessage);
        } else {
            String response = bank.processRequest(customerID, Move.getMenuOption() + emptyString + amount + emptyString + from + emptyString + to);
            out.println(response);
        }
    }

    public void payMoneyToCustomer(BufferedReader in , PrintWriter out, CustomerID customerID, NewBank bank) {
        String recipient;
        String amount;
        String fromAccount;

        out.println(requestFrom + customerID.getKey());


        recipient = verifyCustomer(out, in , bank);
        amount = verifyAmount(out, in );
        Double dAmount = Double.valueOf(amount);
        fromAccount = verifyFromAccount(out, in , customerID, bank);

        if (!verifyPayerSufficientBalance(out, customerID, bank, dAmount, fromAccount)) {
            out.println(accountLabel + fromAccount + insufficientBalanceMessage);
        } else {
            String response = bank.processRequest(customerID, Pay.getMenuOption() + emptyString + recipient + emptyString + amount + emptyString + fromAccount);
            verifyAmountBiggerThanEmailNotificationThreshold(out, dAmount, customerID, bank);
            out.println(response);
        }
    }

    public void getCustomerDetails(PrintWriter out, CustomerID customerID, NewBank bank) {
        out.println(retrievingCustomerDetails);
        out.println(customerDetailsMessage);
        out.println(lineSeparator);
        out.println(bank.processRequest(customerID, CustomerDetails.getMenuOption()));
        out.println(lineSeparator);
        out.println(done);
    }


    /*
       Helper functions
    */

    private boolean isValidName(String s) {
        if (s == null) { // checks if the String is null
            return false;
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            // checks whether the character is not a letter
            // if it is not a letter ,it will return false
            if ((!Character.isLetter(s.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    private String verifyAmount(PrintWriter out, BufferedReader in ) {
        out.println(enterAmount);
        String amount = "";
        while (true) {
            try {
                amount = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!iPaymentHelper.isNumeric(out, amount)) {
                out.println(amount + nonNumericalAmount);
            } else {
                return amount;
            }
        }
    }

    private String verifyFromAccount(PrintWriter out, BufferedReader in , CustomerID customerID, NewBank bank) {
        HashMap < String, Customer > map = bank.getCustomers();
        Customer customer = map.get(customerID.getKey());
        String from = "";
        out.println(enterFromAccountName);
        while (true) {
            try {
                from = in .readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Account account = customer.getHasMapForAllCustomerAccounts().get(from);
            if (account == null) {
                out.println(from + accountNotFound);
            }
            else {
                return from;
            }
        }
    }

    private String verifyToAccount(PrintWriter out, BufferedReader in , CustomerID customerID, NewBank bank) {
        HashMap < String, Customer > map = bank.getCustomers();
        Customer customer = map.get(customerID.getKey());
        String to = "";
        out.println(enterToAccountName);
        while (true) {
            try {
                to = in .readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Account account = customer.getHasMapForAllCustomerAccounts().get(to);
            if (account == null) {
                out.println(to + accountNotFound);
            }
            else {
                return to;
            }

        }
    }

    private String verifyCustomer(PrintWriter out, BufferedReader in , NewBank bank) {
        String recipient = "";
        out.println(enterRecipientName);
        while (true) {
            try {
                recipient = in .readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Customer customer = bank.getCustomers().get(recipient);
            if (customer == null) {
                out.println(recipient + customerNotFound);
            }
            else {
                return recipient;
            }
        }
    }

    private String verifyNewAccountName(PrintWriter out, BufferedReader in ) {
        String newAccount = "";

        out.println(newAccountName);
        while (true) {
            try {
               newAccount = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!isValidName(newAccount)) {
                out.println(newAccount + invalidAccountNameCharacters);
            } else {
                return newAccount;
            }
        }
    }

    private Boolean verifyPayerSufficientBalance(PrintWriter out, CustomerID customerID, NewBank bank, Double amount, String fromAccount) {
        HashMap < String, Customer > map = bank.getCustomers();
        Customer customer = map.get(customerID.getKey());
        return iPaymentHelper.checkAccountHasSufficientBalance(out, customer.getHasMapForAllCustomerAccounts().get(fromAccount), amount);
    }

    private void verifyAmountBiggerThanEmailNotificationThreshold(PrintWriter out, Double amount, CustomerID customerID, NewBank bank){
        HashMap < String, Customer > map = bank.getCustomers();
        Customer customer = map.get(customerID.getKey());
        String emailTo = customer.getEmail();
        if (amount > 49){
            EmailHandler.sendEmail(out, emailTo, "test", "test");
        }
    }
}