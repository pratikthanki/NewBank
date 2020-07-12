package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

    private NewBank bank;
    private BufferedReader in ;
    private PrintWriter out;


    public NewBankClientHandler(Socket s) throws IOException {
        bank = NewBank.getBank(); in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
    }

    public void run() {
        // keep getting requests from the client and processing them
        try {
            // ask for user name
            out.println("Enter Username");
            String userName = in .readLine();
            // ask for password
            out.println("Enter Password");
            String password = in .readLine();
            out.println("Checking Details...");
            // authenticate user and get customer ID token from bank for use in subsequent requests
            CustomerID customer = bank.checkLogInDetails(userName, password);
            // if the user is authenticated then get requests from the user and process them
            if (customer != null) {
                String[] menuOptions = {
                        "Show my accounts",
                        "New Account",
                        "Move",
                        "Pay"
                };
                String[] requests = {
                        "SHOWMYACCOUNTS",
                        "NEWACCOUNT",
                        "MOVE",
                        "PAY"
                };
                out.println("Log In Successful. Please choose one of the following options:");
                for (int i = 0; i < menuOptions.length; i++) {
                    String item = menuOptions[i];
                    out.println(i + ".\t" + item);
                }
                out.println("9.\tQuit");
                // handle user commands
                String menuItem;
                do {
                    out.println("\nPlease select a new option:");
                    menuItem = in .readLine();
                    switch (menuItem) {
                        case "0":
                            out.println("Request from " + customer.getKey());
                            String response = bank.processRequest(customer, requests[0]);
                            out.println(response);
                            break;
                        case "9":
                            out.println("Bye-bye!");
                            break;
                        default:
                            out.println("Invalid choice.");
                    }
                } while (!menuItem.equals("9"));
            } else {
                out.println("Log In Failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in .close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

}