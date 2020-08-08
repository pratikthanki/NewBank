package newbank.server.authentication;

import newbank.database.DatabaseClient;
import newbank.server.CustomerID;

import java.net.Authenticator;


public class BasicAuthenticator extends Authenticator {
    private String userName, password;
    private final DatabaseClient databaseClient;

    public BasicAuthenticator(final String userName, final String password, DatabaseClient databaseClient) {
        this.userName = userName;
        this.password = password;
        this.databaseClient = databaseClient;
    }

    public CustomerID ValidateLogin() {
        if (databaseClient.getCustomerById(this.userName) == null) return null;

        String pwd = databaseClient.getCustomerById(this.userName).getPassword();
        if (pwd.equals(this.password)) return new CustomerID(this.userName);

        return null;
    }
}