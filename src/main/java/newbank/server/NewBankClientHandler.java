package newbank.server;

//import org.graalvm.compiler.lir.StandardOp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class NewBankClientHandler extends Thread {

    private NewBank bank;
    private BufferedReader in ;
    private PrintWriter out;

    public NewBankClientHandler(Socket s) throws IOException {
        bank = NewBank.getBank(); 
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
    }

    public void run() {
        // keep getting requests from the client and processing them
        try {
            //ask for user name
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
                out.println("Log In Successful. Please choose one of the following options using :");
                
                // handle user commands
                handleUserCommands( in , customer);

            } else {
                out.println("Log In Failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
                 finally {
                    try {
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
    }

    private void printMenu(HashMap < String, String > hashMap) {
        // Print values
        int count = 0;
        for (String key: hashMap.values()) {
            count++;
            out.println(count + ".\t" + key);
        }
        out.println("9.\tQuit");
    }

    private void handleUserCommands(BufferedReader in , CustomerID customer) throws IOException {
       
    	String menuItem;
        do {
      
        	//Menu options and requests
		   HashMap < String, String > menuOptions = new HashMap < > ();
		   menuOptions.put("1", "SHOWMYACCOUNTS");
		   menuOptions.put("2", "NEWACCOUNT");
		   menuOptions.put("3", "MOVE");
		   menuOptions.put("4", "PAY");
		   menuOptions.put("5", "CUSTOMERDETAIL");
		   menuOptions.put("6", "UPDATECUSTOMERDETAIL");
		   
		   printMenu(menuOptions);
               
           out.println("\nPlease select a new option:");
            
            menuItem = in .readLine();

            switch (menuItem) {
                case "1":
                    out.println("Request from " + customer.getKey());
                    String response = bank.processRequest(customer, menuOptions.get("1"));
                    out.println(response);
                    break;
                case "2":
                    out.println("Request from " + customer.getKey());
                    out.println("Please enter the new account's name:");
                    String newAccountName = in .readLine();
                    String response2 = bank.processRequest(customer,menuOptions.get("2") + " " + newAccountName);
                    out.println(response2);
                    break;
                case "3":
                    out.println("Request from " + customer.getKey());
                    out.println("Please follow the instructions below to complete your MOVE. You need to enter the amount, outgoing and receiving account.\nPlease enter the amount you would like to move:");
                    String amount = in .readLine();
                    out.println("Please enter the outgoing account's name");
                    String from= in .readLine();
                    out.println("Please enter the receiving account's name");
                    String to = in .readLine();
                    String response3 = bank.processRequest(customer,menuOptions.get("3") + " " + amount + " " + from + " " + to );
                    out.println(response3);
                    break;
                case "5":
                	out.println("Retrieving customer detail...");
                    out.println("CUSTOMER DETAIL");
                    out.println("----------------------");
                    out.println(bank.processRequest(customer, menuOptions.get("5")));
                    out.println("----------------------");
                    out.println("Done.");
                    
                    break;
                case "6":
                	//Menu options and requests
                	menuOptions.clear(); 
                	menuOptions.put("1", "UPDATECUSTOMEREMAIL");
                	menuOptions.put("2", "UPDATECUSTOMERADDRESS");
                	menuOptions.put("3", "UPDATECUSTOMERDOB");
                	menuOptions.put("4", "UPDATECUSTOMERNAME");
         		   
                	out.println("Please select data to update:");
         		    printMenu(menuOptions);
         		    
         		    menuItem = in .readLine();
         		    
         		   switch (menuItem) {
         		   		case "1":
         		   			out.println("The current email address is: " + bank.processRequest(customer, "GETCUSTOMEREMAIL") );
         		   			out.println("Please type in the new email address: ");
         		   			String email = in .readLine();
         		   			String emailResponse = bank.processRequest(customer, menuOptions.get("1") + " " + email);
         		   			out.println(emailResponse);
         		   			break;
         		   			
         		   		case "2":
	         		   		out.println("The current address is: " + bank.processRequest(customer, "GETCUSTOMERADDRESS") );
	     		   			out.println("Please type in the new address: ");
	     		   			String address = in .readLine();
	     		   			String addressResponse = bank.processRequest(customer, menuOptions.get("2") + " " + address);
	     		   			out.println(addressResponse);
	     		   			break;
	     		   			
         		   		case "3":
	         		   		out.println("The current DOB is: " + bank.processRequest(customer, "GETCUSTOMERDOB") );
	     		   			out.println("Please type in the new DOB: " + new SimpleDateFormat("d MM yyyy").toPattern() );
	     		   			String dob = in .readLine();
	     		   			String dobResponse = bank.processRequest(customer, menuOptions.get("3") + " " + dob);
	     		   			out.println(dobResponse);
	     		   			break;
	         		   	case "4":
	         		   		out.println("The current name is: " + bank.processRequest(customer, "GETCUSTOMERNAME") );
	     		   			out.println("Please type in the new name: ");
	     		   			String newName = in .readLine();
	     		   			String nameResponse = bank.processRequest(customer, menuOptions.get("4") + " " + newName);
	     		   			out.println(nameResponse);
	     		   			break;
	     		   			
	         		   	case "9":
	                        out.println("Bye-bye!");
	                        break;
                   
         		   		default:
         		   			out.println("Invalid choice.");
         		   }
         		    
                    break;
                case "9":
                    out.println("Bye-bye!");
                    break;
                default:
                    out.println("Invalid choice.");
            }
        } while (!menuItem.equals("9"));
    }

}                
         		  ile (!menuItem.equals("9"));
    }

}