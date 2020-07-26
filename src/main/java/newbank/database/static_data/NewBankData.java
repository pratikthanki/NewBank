package newbank.database.static_data;

import newbank.server.CustomerID;

public class NewBankData {

    //Transaction statuses
    public enum Statuses{
        SUCCESS,FAIL
    }

    //Menu options
    public final static String showMyAccounts = "SHOWMYACCOUNTS";
    public final static String newAccount = "NEWACCOUNT";
    public final static String move = "MOVE";
    public final static String pay = "PAY";
    public final static String customerDetail = "CUSTOMERDETAIL";

    //Command syntax
    public static String moveWrongSyntaxMessage = "You have not provided all the required values to transfer money between your accounts. " +
            "Please provide the request in the following format: MOVE <Amount> <FromAccount> <ToAccount>";
    public static String payWrongSyntaxMessage = "You have not provided all the required values to PAY money to another customer " +
            "Please provide the request in the following format: PAY <CustomerName> <Amount> <AccountFrom>";

    //User interactions messages:
    public static String enterUsername = "Enter Username";
    public static String enterPassword = "Enter Password";
    public static String checkingDetailsMessage = "Checking Details...";
    public static String loginSuccessfulMessage = "Login Successful. Enter a number to select one of the options:";
    public static String loginFailedMessage = "Login Failed";
    public static String selectOption = "\nPlease select an option:";
    public static String requestFrom = "Request from: ";
    public static String newAccountName = "Please enter the new account's name:";
    public static String enterAmount = "Please enter amount:";
    public static String enterFromAccountName = "Please enter the FromAccount's name:";
    public static String enterToAccountName = "Please enter the ToAccount's name:";
    public static String enterRecipientName = "Please enter the Recipient name:";
    public static String retrievingCustomerDetails = "Retrieving customer detail...";
    public static String customerDetailsMessage = "CUSTOMER DETAIL";
    public static String lineSeparator = "----------------------";
    public static String bye = "Bye-bye!";
    public static String quit = ".\tQuit";
    public static String done = "Done.";
    public static String invalidChoice = "Invalid choice.";
    public static String emptyString = " ";
    public static String accountLabel = "Account: ";
    public static String insufficientBalanceMessage = " does not have sufficient balance for this transaction.";
    public static String invalidAccountNameCharacters = " is not a valid account name. Please try again,  using alphabetical characters only.";
    public static String nonNumericalAmount = " is not a valid amount. Please try again, using numerical characters only:";
    public static String accountNotFound = " is not a valid account. Please try again:";
    public static String customerNotFound = " is not a member of New Bank. Please try again:";












    //IDs
    public final static String one = "1";
    public final static String two = "2";
    public final static String three = "3";
    public final static String four = "4";
    public final static String five = "5";
    public final static String six = "6";
    public final static String seven = "7";
    public final static String eight = "8";
    public final static String nine = "9";
    public final static String ten = "10";

}
