package newbank.server;

import newbank.database.DatabaseClient;
import newbank.database.HibernateDatabaseClient;
import newbank.database.HibernateUtility;
import newbank.server.authentication.BasicAuthenticator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static newbank.database.static_data.NewBankData.*;

public class NewBank {

    private static NewBank bank;
    private static final IPaymentHelper paymentHelper = new IPaymentHelper();
    private final DatabaseClient databaseClient;
    private BasicAuthenticator basicAuthenticator;

    private NewBank(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public static NewBank getBank() {
        if (bank == null) {
            bank = new NewBank(new HibernateDatabaseClient(HibernateUtility.development()));
        }
        return bank;
    }

    public static void init(DatabaseClient databaseClient) {
        if (bank == null) {
            bank = new NewBank(databaseClient);
        }
    }

    public synchronized CustomerID checkLogInDetails(String userName, String password) {
        basicAuthenticator = new BasicAuthenticator(userName, password, databaseClient);

        return basicAuthenticator.ValidateLogin();
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customerID, String request) {
        if (databaseClient.hasCustomer(customerID.getKey())) {
            switch (parseString(request)[0]) {
                case showMyAccounts:
                    return showMyAccounts(customerID);
                case newAccount:
                    return createNewAccount(customerID, request);
                case move:
                    return moveMoney(customerID, request);
                case pay:
                    return payMoney(customerID, request);
                case customerDetail:
                    return getCustomer(customerID).getDetail();
                case showCustomerName:
                    return getCustomer(customerID).getName();
                case showCustomerEmail:
                    return getCustomer(customerID).getEmail();
                case showCustomerDOB:
                    return new SimpleDateFormat("d MM yyyy").format(getCustomer(customerID).getDob());
                case showCustomerAddress:
                    return getCustomer(customerID).getAddress();
                default:
                    return Status.FAIL.toString();
            }
        }
        return Status.FAIL.toString();
    }

    // commands from the NewBank customer are processed in this method
    public synchronized String processRequest(CustomerID customer, String command, Map<Parameter, String> properties) {
        if (databaseClient.hasCustomer(customer.getKey())) {
            switch (command) {
                case updateCustomerEmail:
                    return updateCustomerEmail(customer, command, properties);
                case updateCustomerAddress:
                    return updateCustomerAddress(customer, command, properties);
                case updateCustomerDOB:
                    return updateCustomerDob(customer, command, properties);
                case updateCustomerName:
                    return updateCustomerName(customer, command, properties);
                default:
                    return Status.FAIL.toString() + ": Invalid Choice.";
            }
        }
        return "FAIL";
    }

    private Customer getCustomer(CustomerID customer) {
        return databaseClient.getCustomerById(customer);
    }

    private String createNewAccount(CustomerID customerID, String request) {
        String[] requestAndDetails = request.split(emptyString);
        if (requestAndDetails.length == 2) {
            String newAccountName = requestAndDetails[1];
            databaseClient.addAccount(customerID, new Account(newAccountName, 0.0, 1234));
            return Status.SUCCESS.toString();
        }
        return Status.FAIL.toString();
    }

    private String showMyAccounts(CustomerID customer) {
        return databaseClient.getAccountsAsString(customer);
    }

    private String moveMoney(CustomerID customerID, String request) {
        //User input
        String[] parsedInput = parseString(request);

        if (parsedInput.length != 4) return Status.FAIL.toString();

        //customer
        Customer customer = databaseClient.getCustomerById(customerID);

        //amount
        String userInputAmount = parsedInput[1];
        if (!validUserAmount(userInputAmount)) return Status.FAIL.toString();
        Double amount = Double.parseDouble(userInputAmount);

        HashMap<String, Account> customerAccounts = customer.getHasMapForAllCustomerAccounts();

        //Get the 'from' account
        Account from = customerAccounts.get(parsedInput[2]);

        //Get the 'to' account
        Account to = customerAccounts.get(parsedInput[3]);

        if (from == null || to == null) return Status.FAIL.toString();

        //Calculate transaction
        paymentHelper.calculateTransaction(from, to, amount);
        return Status.SUCCESS.toString();
    }

    //pay another person
    private String payMoney(CustomerID customerID, String request) {
        //User input
        String[] parsedInput = parseString(request);
        if (parsedInput.length != 4) return Status.FAIL.toString();

        //payer
        Customer payer = databaseClient.getCustomerById(customerID);
        HashMap<String, Account> payerAccounts = payer.getHasMapForAllCustomerAccounts();

        //payee
        String payeeCustomerName = parsedInput[1];
        Customer payee = databaseClient.getCustomerById(payeeCustomerName);
        if (payee == null) return Status.FAIL.toString();

        HashMap<String, Account> payeeAccounts = payee.getHasMapForAllCustomerAccounts();
        if (payeeAccounts.size() == 0) return Status.FAIL.toString();

        //check if amount is a valid numerical value
        String userInputAmount = parsedInput[2];

        if (!validUserAmount(userInputAmount)) return Status.FAIL.toString();
        Double amount = Double.parseDouble(userInputAmount);

        //Get the 'from' account
        Account from = payerAccounts.get(parsedInput[3]);

        //Get payee's default account
        Account to = payee.getDefaultAccount();

        if (from == null || to == null) return Status.FAIL.toString();

        //Calculate transaction
        paymentHelper.calculateTransaction(from, to, amount);
        databaseClient.updateCustomer(payer);
        databaseClient.updateCustomer(payee);
        return Status.SUCCESS.toString();
    }

    private String updateCustomerEmail(CustomerID customer, String command, Map<Parameter, String> properties) {
        String newEmail = properties.get(Parameter.EMAIL);
        if (InputValidator.isEmailAddressValid(newEmail)) {
            Customer theCustomer = databaseClient.getCustomerById(customer.getKey());
            theCustomer.setEmail(newEmail);
            databaseClient.updateCustomer(theCustomer);
            return Status.SUCCESS.toString();
        }
        return Status.FAIL.toString();
    }

    private String updateCustomerAddress(CustomerID customer, String command, Map<Parameter, String> properties) {
        String newAddress = properties.get(Parameter.ADDRESS);

        if (InputValidator.validateTextLength(newAddress, 4, -1)) {
            Customer theCustomer = databaseClient.getCustomerById(customer.getKey());
            theCustomer.setAddress(newAddress);
            databaseClient.updateCustomer(theCustomer);
            return Status.SUCCESS.toString();
        }

        return Status.FAIL.toString();
    }

    private String updateCustomerName(CustomerID customer, String command, Map<Parameter, String> properties) {
        String newName = properties.get(Parameter.FIRSTNAME) + " " + properties.get(Parameter.SURNAME);

        if (InputValidator.validateTextLength(newName, 4, -1)) {
            Customer theCustomer = databaseClient.getCustomerById(customer.getKey());
            theCustomer.setName(newName);
            databaseClient.updateCustomer(theCustomer);
            return Status.SUCCESS.toString();
        }
        return Status.FAIL.toString();
    }

    private String updateCustomerDob(CustomerID customer, String command, Map<Parameter, String> properties) {
        String dob = properties.get(Parameter.DOB);

        if (InputValidator.parseDate(dob, "d MM yyyy") == null) return "FAIL: Invalid date format for Date of Birth";

        try {
            SimpleDateFormat parser = new SimpleDateFormat("d MM yyyy");

            Date d;
            d = parser.parse(dob);
            Customer theCustomer = databaseClient.getCustomerById(customer.getKey());
            theCustomer.setDob(d);
            databaseClient.updateCustomer(theCustomer);
            return Status.SUCCESS.toString();

        } catch (ParseException e) {
            return Status.FAIL.toString() + ": Date format error.";
        }
    }

    public String registerNewCustomer(Map<String, String> properties) {
        String firstname = properties.get("firstname");
        String surname = properties.get("surname");
        String newPassword = properties.get("password1");
        String confirmPassword = properties.get("password2");
        String dob = properties.get("dob");
        String email = properties.get("email");
        String address = properties.get("address");

        if (InputValidator.parseDate(dob, "d MM yyyy") == null) return "FAIL: Invalid Date of Birth.";
        if (!(newPassword.equals(confirmPassword)))
            return "FAIL: Password mismatch." + newPassword + " " + confirmPassword;
        if (!InputValidator.isEmailAddressValid(email)) return "FAIL: Invalid email address.";
        if (!InputValidator.validateTextLength(address, 4, -1)) return "FAIL: Invalid address length.";

        Customer customer = new Customer(firstname, surname);

        if (InputValidator.parseDate(dob, "d MM yyyy") == null)
            return Status.SUCCESS.toString() + ": Invalid Date Format";

        customer.setDob(InputValidator.parseDate(dob, "d MM yyyy"));
        customer.setEmail(email);
        customer.setAddress(address);

        if (!(customer.setPassword(confirmPassword))) {
            return Status.FAIL.toString() + ":Password Rejected";
        }

        databaseClient.addNewCustomer(customer);

        return Status.SUCCESS.toString();
    }

    public boolean validUserAmount(final String s) {
        try {
            final double move = Double.parseDouble(s);
            return true;
        } catch (final NumberFormatException e) {
            System.out.printf("Error: invalid input \"%s\", please try again.\n", s);
        }
        return false;
    }

    private String[] parseString(String inputString) {
        return inputString.split(emptyString);
    }

    public Customer getCustomerById(String customerID) {
        return databaseClient.getCustomerById(customerID);
    }
}