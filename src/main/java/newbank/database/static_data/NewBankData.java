package newbank.database.static_data;

import newbank.server.CustomerID;

public abstract class NewBankData {

    //Transaction statuses
    public enum Status {
        SUCCESS,FAIL
    }

    //Menu options
    public final static String showMyAccounts = "SHOWMYACCOUNTS";
    public final static String newAccount = "NEWACCOUNT";
    public final static String move = "MOVE";
    public final static String pay = "PAY";
    public final static String customerDetail = "CUSTOMERDETAIL";

    //Menu Option IDs
    public final static String enumIdOne = "1";
    public final static String enumIdTwo = "2";
    public final static String enumIdThree = "3";
    public final static String enumIdFour = "4";
    public final static String enumIdFive = "5";
    public final static String enumIdSix = "6";
    public final static String enumIdSeven = "7";
    public final static String enumIdEight = "8";
    public final static String enumIdNine = "9";
    public final static String enumIdTen = "10";

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
}
