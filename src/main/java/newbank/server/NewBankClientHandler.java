package newbank.server;

import newbank.database.static_data.NewBankData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import static newbank.database.static_data.NewBankData.*;

public class NewBankClientHandler extends Thread {

    private final NewBank bank;
    private final BufferedReader in;
    private final PrintWriter out;

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
                handleUserCommands(in, customer);

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

    private void processMenuSelection(HashMap<String, String> hashMap) {
        // Print values
        int count = 0;
        for (String key : hashMap.values()) {
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

    private void handleUserCommands(BufferedReader in, CustomerID customerID) throws IOException {
        String menuItem;
        do {
            processMenuSelection(getMainMenu());
            out.println(selectOption);
            menuItem = in.readLine();
            switch (menuItem) {
                case one:
                    out.println(requestFrom + customerID.getKey());
                    String response = bank.processRequest(out, customerID, getMainMenu().get(one));
                    out.println(response);
                    break;
                case two:
                    out.println(requestFrom + customerID.getKey());
                    out.println(newAccountName);
                    String newAccountName = in.readLine();
                    String response2 = bank.processRequest(out, customerID, getMainMenu().get(two) + " " + newAccountName);
                    out.println(response2);
                    break;
                case three:
                    out.println(requestFrom + customerID.getKey());
                    out.println(enterAmount);
                    String amount = in.readLine();
                    out.println(enterFromAccountName);
                    String from = in.readLine();
                    out.println(enterToAccountName);
                    String to = in.readLine();
                    String response3 = bank.processRequest(out, customerID, getMainMenu().get(three) + " " + amount + " " + from + " " + to);
                    out.println(response3);
                    break;
                case four:
                    out.println(requestFrom + customerID.getKey());
                    out.println(enterRecipientName);
                    String to1 = in.readLine();
                    out.println(enterAmount);
                    String amount1 = in.readLine();
                    out.println(enterFromAccountName);
                    String from1 = in.readLine();
                    String response4 = bank.processRequest(out, customerID, getMainMenu().get(four) + " " + to1 + " " + amount1 + " " + from1);
                    out.println(response4);
                    break;
                case five:
                    out.println(retrievingCustomerDetails);
                    out.println(customerDetailsMessage);
                    out.println(lineSeparator);
                    out.println(bank.processRequest(out, customerID, getMainMenu().get(five)));
                    out.println(lineSeparator);
                    out.println(done);

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