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
    
    public void updateCustomerEmail(BufferedReader in, PrintWriter out, CustomerID customerID, NewBank bank) {
    	HashMap<Parameter, String> properties = new HashMap<>();
    	out.println("Retrieving customer detail...");
        out.println("The old email address is: " + bank.processRequest(customerID, showCustomerEmail));
        out.println("Please enter the new email address: ");
        
		try {
			properties.put(Parameter.EMAIL, in.readLine());
			out.println("Updating EMAIL ADDRESS...");
	        out.println(bank.processRequest(customerID, updateCustomerEmail, properties));
	        out.println("----------------------");
	        out.println("Done.");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void updateCustomerAddress(BufferedReader in, PrintWriter out, CustomerID customerID, NewBank bank) {
    	HashMap<Parameter, String> properties = new HashMap<>();
    	out.println("Retrieving customer detail...");
        out.println("The old address is: " + bank.processRequest(customerID, showCustomerAddress));
        out.println("Please enter the new ADDRESS: ");
        
		try {
			properties.put(Parameter.ADDRESS, in.readLine());
			out.println("Updating ADDRESS...");
	        out.println(bank.processRequest(customerID, updateCustomerAddress, properties));
	        out.println("----------------------");
	        out.println("Done.");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }
    private void updateCustomerDOB(BufferedReader in, PrintWriter out, CustomerID customerID, NewBank bank) {
    	HashMap<Parameter, String> properties = new HashMap<>();
		
		try {
			out.println("The current Date of Birth is : " + bank.processRequest(customerID, showCustomerDOB)) ;
			
			out.println("Enter Customer Date of Birth");
			properties.put(Parameter.DOB, in.readLine());
			
			out.println(bank.processRequest(customerID, updateCustomerDOB, properties));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private void updateCustomerName(BufferedReader in, PrintWriter out, CustomerID customerID, NewBank bank) {
		HashMap<Parameter, String> properties = new HashMap<>();
		
		try {
			out.println("The current name is : " + bank.processRequest(customerID, showCustomerName)) ;
			
			out.println("Enter Customer FIRSTNAME");
			properties.put(Parameter.FIRSTNAME, in.readLine());
			
			out.println("Enter Customer SURNAME");
			properties.put(Parameter.SURNAME, in.readLine());
			
			out.println(
					bank.processRequest(customerID, updateCustomerName, properties));
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
    public void updateCustomerDetails(BufferedReader in, PrintWriter out, CustomerID customerID, NewBank bank) {

    	 String menuItem;
    	 
    	 UpdateCustomerDetailsMenu.processMenuSelection(out);
    	 
         out.println(selectOption);
         try {
			menuItem = in.readLine();
			
			switch (menuItem) {
				case enumIdOne:
	                this.updateCustomerName(in, out, customerID, bank);
	                break;
	            case enumIdTwo:
	                this.updateCustomerEmail(in, out, customerID, bank);
	                break;
	            case enumIdThree:
	                this.updateCustomerAddress(in, out, customerID, bank);
	                break;
	            case enumIdFour:
	                this.updateCustomerDOB(in, out, customerID, bank);
	                break;
	            default:
	            	out.println(invalidChoice);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
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
}