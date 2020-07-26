package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Menu {

	public static MenuItem present(MenuOptions menuOption, PrintWriter out, BufferedReader in ) {
		Map<String, MenuItem> options;
		
		try {
			switch (menuOption) {
				case INITIAL_OPTIONS:
					options = initialMenuOptions();
					addQuitOptionToMenu(options);
					print(out, options);
					out.println("Please select an option: ");
					return acceptInput(out, in, options);
				case AUTHENTICATED_USER:
					options = authenticatedUserMenu();
					addQuitOptionToMenu(options);
					print(out, options);
					out.println("Please select an option: ");
					return acceptInput(out, in, options);
				case UPDATE_CUSTOMER_DETAIL:
					options = updateCustomerDetailMenu();
					addQuitOptionToMenu(options);
					print(out, options);
					out.println("Please select an option: ");
					return acceptInput(out, in, options);
				default:
					break;
			}
		} catch (IOException e) {
			e.printStackTrace(out);
		}
		
		return null;
	}
	
	private static MenuItem acceptInput(PrintWriter out, BufferedReader in, Map<String, MenuItem> options)
			throws IOException {
		String selectedOption = in.readLine();
		
		//check if a valid option was selected
		while(!options.containsKey(selectedOption)) {
			out.println("Try again! \n Please select a valid option: ");
			selectedOption = in.readLine();
		}
		
		MenuItem menuItem = options.get(selectedOption);
		
		if (!menuItem.hasSubMenu() ) {
			for (CommandParameter cp : menuItem.getCommandParameters().values()) {
				out.println(cp.getPrompt());
				String input = in.readLine();
				//you may validate parameter before setting value
				cp.setValue(input);
				//effectively, the code returns here, if no submenu.
			}
		} else {
			return present(menuItem.getMenuOption(),out, in);
		}
		return menuItem;
	}

	private static void print(PrintWriter out, Map<String, MenuItem> menuOptions) {
        // Print values
		StringBuilder stringBuilder = new StringBuilder();
		
        for (String key: menuOptions.keySet()) 
            stringBuilder.append(key + ".\t" + menuOptions.get(key).getLabel() + "\n");
        
        out.println(stringBuilder.toString());
    }
	
	private static void addQuitOptionToMenu(Map<String, MenuItem> options) {
		options.put("9", new MenuItem(MenuOptions.DEFAULT, Command.QUIT, "QUIT", "Quit Application"));
	}

	private static Map<String, MenuItem> initialMenuOptions(){
		Map < String, MenuItem > menuOptions = new LinkedHashMap < > ();
		
		MenuItem loginMenuItem = new MenuItem(MenuOptions.INITIAL_OPTIONS, Command.LOGIN, "LOGIN", "Registered user login");
		loginMenuItem.addCommandParameter(new CommandParameter(Parameter.USERNAME,"","Please enter your username: ","String"));
		loginMenuItem.addCommandParameter(new CommandParameter(Parameter.PASSWORD,"","Please enter your password: ","String"));
		
		menuOptions.put("1",loginMenuItem);
		
		MenuItem newUserMenuItem = new MenuItem(MenuOptions.INITIAL_OPTIONS, Command.NEW_USER_REGISTRATION,"NEWUSERREGISTRATION", "Register new user");
		newUserMenuItem.addCommandParameter(new CommandParameter(Parameter.FIRSTNAME,"","Please enter your firstname: ","String"));
		newUserMenuItem.addCommandParameter(new CommandParameter(Parameter.SURNAME,"","Please enter your surname: ","String"));
		newUserMenuItem.addCommandParameter(new CommandParameter(Parameter.NEW_PASSWORD,"","Please enter your password: ","String"));
		newUserMenuItem.addCommandParameter(new CommandParameter(Parameter.CONFIRM_PASSWORD,"","Please re-enter your password: ","String"));
		newUserMenuItem.addCommandParameter(new CommandParameter(Parameter.DOB,"","Please enter your Date of Birth: ","String"));
		newUserMenuItem.addCommandParameter(new CommandParameter(Parameter.EMAIL,"","Please enter your email address: ","String"));
		newUserMenuItem.addCommandParameter(new CommandParameter(Parameter.ADDRESS,"","Please enter your home address: ","String"));
		
		menuOptions.put("2", newUserMenuItem);
		
		return menuOptions;
	} 
	
	private static Map<String, MenuItem>  authenticatedUserMenu(){
		Map < String, MenuItem > authUserMenuOptions = new LinkedHashMap < > ();
		authUserMenuOptions.put("1", 
				new MenuItem(MenuOptions.AUTHENTICATED_USER,Command.SHOWMYACCOUNTS,"SHOWMYACCOUNTS", "Accounts", null));
		
		MenuItem newAcctItem = new MenuItem(MenuOptions.AUTHENTICATED_USER, Command.NEWACCOUNT, "NEWACCOUNT", "New Account");
		newAcctItem.addCommandParameter(new CommandParameter(Parameter.ACCOUNT_NAME,"","Please enter the new account's name: ","String"));
		authUserMenuOptions.put("2",newAcctItem);
		
		MenuItem move = new MenuItem(MenuOptions.AUTHENTICATED_USER, Command.MOVE, "MOVE","Move Money");
		move.addCommandParameter(new CommandParameter(Parameter.AMOUNT,"","\"Please follow the instructions below to complete your MOVE. You need to enter the amount, outgoing and receiving account.\\nPlease enter the amount you would like to move:\n","String"));
		move.addCommandParameter(new CommandParameter(Parameter.FROM_ACCOUNT,"","Please enter the outgoing account's name: ","String"));
		move.addCommandParameter(new CommandParameter(Parameter.TO_ACCOUNT,"","Please enter the receiving account's name: ","String"));
		authUserMenuOptions.put("3", move);
		
		MenuItem pay = new MenuItem(MenuOptions.AUTHENTICATED_USER, Command.PAY, "PAY","Pay Money");
		pay.addCommandParameter(new CommandParameter(Parameter.AMOUNT,"",".\nPlease enter the amount to PAY: \n","String"));
		pay.addCommandParameter(new CommandParameter(Parameter.FROM_ACCOUNT,"","Which account account are you paying from? (e.g. Savings, Checking): ","String"));
		pay.addCommandParameter(new CommandParameter(Parameter.PAYEE_CUSTOMER_NAME,"","Which Customer are you paying? ","String"));
		authUserMenuOptions.put("4", pay);
		
		authUserMenuOptions.put("5", 
				new MenuItem(MenuOptions.AUTHENTICATED_USER,Command.SHOWCUSTOMERDETAIL,"SHOWCUSTOMERDETAIL","Show Customer detail", null));
		
		MenuItem updateUserDetail = new MenuItem(MenuOptions.UPDATE_CUSTOMER_DETAIL,Command.UPDATECUSTOMERDETAIL,"UPDATECUSTOMERDETAIL","Update Customer detail", null);
		updateUserDetail.setSubMenuItems(updateCustomerDetailMenu());
		
		authUserMenuOptions.put("6", updateUserDetail);
		
		return authUserMenuOptions;
	}
	
	private static Map <String, MenuItem > updateCustomerDetailMenu(){
		Map < String, MenuItem > updateCustomerDetailMenuOptions = new LinkedHashMap < > ();
		
		MenuItem updateEmail = new MenuItem(MenuOptions.UPDATE_CUSTOMER_DETAIL, Command.UPDATECUSTOMEREMAIL,"UPDATECUSTOMEREMAIL", "Update Customer Email",null);
		updateEmail.addCommandParameter(new CommandParameter(Parameter.EMAIL,"","Please enter the email address: ","String"));
		updateCustomerDetailMenuOptions.put("1",updateEmail);
		
		MenuItem updateAddress = new MenuItem(MenuOptions.UPDATE_CUSTOMER_DETAIL, Command.UPDATECUSTOMERADDRESS,"UPDATECUSTOMERADDRESS", "Update Customer Address",null);
		updateAddress.addCommandParameter(new CommandParameter(Parameter.ADDRESS,"","Please enter the new address: ","String"));
		updateCustomerDetailMenuOptions.put("2",updateAddress);
		
		MenuItem updateDob = new MenuItem(MenuOptions.UPDATE_CUSTOMER_DETAIL, Command.UPDATECUSTOMERDOB, "UPDATECUSTOMERDOB", "Update Customer Date of Birth",null);
		updateDob.addCommandParameter(new CommandParameter(Parameter.DOB,"","Please enter the new date of birth:\n ","String"));
		updateCustomerDetailMenuOptions.put("3",updateDob);	
		
		MenuItem updateName = new MenuItem(MenuOptions.UPDATE_CUSTOMER_DETAIL, Command.UPDATECUSTOMERNAME,"UPDATECUSTOMERNAME", "Update Customer Name",null);
		updateName.addCommandParameter(new CommandParameter(Parameter.FIRSTNAME,"","Please enter your FIRSTNAME: \n ","String"));
		updateName.addCommandParameter(new CommandParameter(Parameter.SURNAME,"","Please enter your SURNAME/LASTNAME: \n ","String"));
		updateCustomerDetailMenuOptions.put("4",updateName);
		
		return updateCustomerDetailMenuOptions;
	}

 }
