package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static newbank.database.static_data.NewBankData.*;

public class NewBankClientHandler extends Thread {

    private final NewBank bank;
    private final BufferedReader in;
    private final PrintWriter out;

    IPaymentHelper iPaymentHelper = new IPaymentHelper();
    InputHandler inputHandler = new InputHandler();

    public NewBankClientHandler(Socket s) throws IOException {
        bank = NewBank.getBank();
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
    }

    public void run() {
        // keep getting requests from the client and processing them
        try {
            // ask for user name
            out.println(enterUsername);
            String userName = in.readLine();
            // ask for password
            out.println(enterPassword);
            String password = in.readLine();
            out.println(checkingDetailsMessage);
            // authenticate user and get customer ID token from bank for use in subsequent requests
            CustomerID customer = bank.checkLogInDetails(userName, password);
            // if the user is authenticated then get requests from the user and process them
            if (customer != null) {
                out.println(loginSuccessfulMessage);

                // handle user commands
                handleUserCommands(customer);

            } else {
                out.println(loginFailedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//                 finally {
//                    try {
//                        in.close();
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        Thread.currentThread().interrupt();
//                    }
//                }
    }


    private void handleUserCommands(CustomerID customerID) throws IOException {
        String menuItem;
        do {
            iPaymentHelper.processMenuSelection(out);
            out.println(selectOption);
            menuItem = in.readLine();
            switch (menuItem) {
                case one:
                    inputHandler.showMyAccounts(out, customerID, bank);
                    break;
                case two:
                    inputHandler.newAccount(in, out, customerID, bank);
                    break;
                case three:
                    inputHandler.moveMoneyBetweenAccounts(in,out, customerID, bank);
                    break;
                case four:
                    inputHandler.payMoneyToCustomer(in, out, customerID, bank);
                    break;
                case five:
                    inputHandler.getCustomerDetails(out, customerID, bank);
                    break;
                case nine:
                    out.println(bye);
                    break;
                default:
                    out.println(invalidChoice);
            }
        } while (!menuItem.equals(nine));
    }
}