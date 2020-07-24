package newbank.server.authentication;

import newbank.database.DatabaseClient;
import newbank.server.Customer;
import newbank.server.CustomerID;

import java.net.Authenticator;
import java.util.HashMap;


public class BasicAuthenticator extends Authenticator {
    private String userName, password;
    private DatabaseClient databaseClient = new DatabaseClient();
    private HashMap<String, Customer> customers;

    public BasicAuthenticator(final String userName, final String password) {
        this.userName = userName;
        this.password = password;
        customers = databaseClient.getCustomers();

    }

    public CustomerID ValidateLogin() {
        String pwd = databaseClient.getCustomerByName(this.userName).getPassword();
        if (pwd.equals(this.password)) return new CustomerID(this.userName);

        return null;
    }
}
