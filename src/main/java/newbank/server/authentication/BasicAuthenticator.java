package newbank.server.authentication;

import newbank.database.DatabaseClient;
import newbank.server.CustomerID;

import java.net.Authenticator;


public class BasicAuthenticator extends Authenticator {
    private String userName, password;
    private DatabaseClient databaseClient = new DatabaseClient();

    public BasicAuthenticator(final String userName, final String password) {
        this.userName = userName;
        this.password = password;
    }

    public CustomerID ValidateLogin() {
        if (databaseClient.getCustomerByName(this.userName) == null) return null;

        String pwd = databaseClient.getCustomerByName(this.userName).getPassword();
        if (pwd.equals(this.password)) return new CustomerID(this.userName);

        return null;
    }
}