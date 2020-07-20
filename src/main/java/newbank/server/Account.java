package newbank.server;

public class Account {

    private String accountName;
    private double balance;

    public Account(String accountName, double openingBalance) {
        this.accountName = accountName;
        this.balance = openingBalance;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
