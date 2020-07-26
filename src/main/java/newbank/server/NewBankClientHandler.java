package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
        	//menuItem is return with parameters gathered.
        	MenuItem menuItem = Menu.present(MenuOptions.INITIAL_OPTIONS, out, in);
        	
        	switch (menuItem.getCommand()){
        		case NEW_USER_REGISTRATION: 
        			
        			Map<Parameter,String> properties = new HashMap<>();
        			menuItem.getCommandParameters().forEach((key, commandParameter)-> {
        				properties.put(commandParameter.getParameter(),commandParameter.getValue());
        			});
        			String newUserResponse = bank.registerNewCustomer(properties);
        			out.println(newUserResponse);
        			this.run();
        			break;
        			
        		case LOGIN:
        			String username = menuItem.getCommandParameters().get(Parameter.USERNAME).getValue();
	                String password = menuItem.getCommandParameters().get(Parameter.PASSWORD).getValue();
	                out.println("Checking Details...");
	                // authenticate user and get customer ID token from bank for use in subsequent requests
	                CustomerID customer = bank.checkLogInDetails(username, password);
	                // if the user is authenticated then get requests from the user and process them
	                if (customer != null) {
	                    out.println("Log In Successful. \nPlease choose one of the following options using:\n");
	                    // handle user commands
	                    handleUserCommands( in , customer);

	                } else {
	                    out.println("Log In Failed");
	                }
	        		break;
	        		
        		case QUIT:
        			out.println("Bye bye!");
        			return;
	        	default:
	        		out.println("Invalid Option selected.");
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

    private void handleUserCommands(BufferedReader in , CustomerID customer) throws IOException {
    	//String menuItem;
        do {
        	//Menu options and requests
        	MenuItem menuItem = Menu.present(MenuOptions.AUTHENTICATED_USER, out, in);
        	Command command = menuItem.getCommand();
        	Map<Parameter,String> properties = new HashMap<Parameter,String>();
            
			menuItem.getCommandParameters().forEach((key, commandParameter)-> {
				properties.put(commandParameter.getParameter(),commandParameter.getValue());
			});
        	
            switch (command) {
            	
                case SHOWMYACCOUNTS:
                    out.println("Request from " + customer.getKey());
                    String response = bank.processRequest(customer, command, properties); //properties is not used in SHOWACCPOUNTS
                    out.println(response);
                    break;
                case NEWACCOUNT:
                    out.println("Request from " + customer.getKey());
                    String response2 = bank.processRequest(customer,command, properties);
                    out.println(response2);
                    break;
                case MOVE:
                    out.println("Request from " + customer.getKey());
                    String response3 = bank.processRequest(customer,command,properties );
                    out.println(response3);
                    break;
                case SHOWCUSTOMERDETAIL:
                	out.println("Retrieving customer detail...");
                    out.println("CUSTOMER DETAIL");
                    out.println("----------------------");
                    out.println(bank.processRequest(customer, command, properties));
                    out.println("----------------------");
                    out.println("Done.");
                    break;
                case UPDATECUSTOMEREMAIL:
                	out.println("Retrieving customer detail...");
                    out.println("The old email address is: " + bank.processRequest(customer, Command.GETCUSTOMEREMAIL,properties));
                    out.println("Updating EMAIL ADDRESS...");
                    out.println(bank.processRequest(customer, command, properties));
                    out.println("----------------------");
                    out.println("Done.");
                	break;
                case UPDATECUSTOMERADDRESS:
                	out.println("Retrieving customer detail...");
                    out.println("The old ADDRESS is: " + bank.processRequest(customer, Command.GETCUSTOMERADDRESS,properties));
                    out.println("Updating ADDRESS...");
                    out.println(bank.processRequest(customer, command, properties));
                    out.println("----------------------");
                    out.println("Done.");
                	break;
                case UPDATECUSTOMERDOB:
                	out.println("Retrieving customer detail...");
                    out.println("The old DATE OF BIRTH is: " + bank.processRequest(customer, Command.GETCUSTOMERDOB,properties));
                    out.println("Updating Customer DATE OF BIRTH...");
                    out.println(bank.processRequest(customer, command, properties));
                    out.println("----------------------");
                    out.println("Done.");
                	break;
                case UPDATECUSTOMERNAME:
                	out.println("Retrieving customer detail...");
                    out.println("The old Customer NAME is: " + bank.processRequest(customer, Command.GETCUSTOMERNAME,properties));
                    out.println("Updating Customer NAME...");
                    out.println(bank.processRequest(customer, command, properties));
                    out.println("----------------------");
                    out.println("Done.");
                	break;
                case QUIT:
                    out.println("Bye-bye!");
                    return;
                default:
                    out.println("Invalid command option.");
            }
        } while (true);
    }
}