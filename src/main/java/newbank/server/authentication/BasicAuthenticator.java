package newbank.server.authentication;

import newbank.database.MapDatabaseClient;
import newbank.server.CustomerID;

import java.net.Authenticator;


public class BasicAuthenticator extends Authenticator {
    private String userName, password;
    private MapDatabaseClient databaseClient = new MapDatabaseClient();

    public BasicAuthenticator(final String userName, final String password) {
        this.userName = userName;
        this.password = password;
    }

    public CustomerID ValidateLogin() {
        if (databaseClient.getCustomerById(this.userName) == null) return null;

        String pwd = databaseClient.getCustomerById(this.userName).getPassword();
        if (pwd.equals(this.password)) return new CustomerID(this.userName);

        return null;
    }
}