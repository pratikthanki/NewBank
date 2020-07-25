package newbank.server.accounts;

import newbank.server.Account;

public class CurrentAccount extends Account {

    public CurrentAccount(String accountName, double openingBalance, int accountPin) {
        super(accountName, openingBalance, accountPin);
    }
}
