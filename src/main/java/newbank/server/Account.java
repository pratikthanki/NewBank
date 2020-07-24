package newbank.server;

public class Account {

    private String accountName;
    private double balance;
    private int accountPin;

    public Account(String accountName, double openingBalance, int accountPin) {
        this.accountName = accountName;
        this.balance = openingBalance;
        this.accountPin = accountPin;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void addMoney(double money) {
        balance += money;
    }

    public void withdrawMoney(double money) {
        balance -= money;
    }

    public double getBalance() {
        return balance;
    }

    public String toString() {
        return (accountName + ": " + balance);
    }

}
