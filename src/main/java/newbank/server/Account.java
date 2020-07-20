package newbank.server;

public class Account {

    private String accountName;
    private double balance;
    private boolean defaultAccount;

    public Account(String accountName, double openingBalance) {
        this.accountName = accountName;
        this.balance = openingBalance;
        this.defaultAccount = false;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Boolean isDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(Boolean defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public void addMoney(double money){
        balance += money;
    }

    public void withdrawMoney(double money){
        balance -= money;
    }

    public double getBalance() {
        return balance;
    }

    public String toString() {
        return (accountName + ": " + balance);
    }
}
