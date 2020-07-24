package newbank.server;

public class Account {

    private String accountName;
    private double balance;
    private boolean defaultAccountStatus;

    public Account(String accountName, double openingBalance) {
        this.accountName = accountName;
        this.balance = openingBalance;
        this.defaultAccountStatus = false;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Boolean isDefaultAccount() {
        return defaultAccountStatus;
    }

    public void setDefaultAccount(Customer customer) {
        int count = customer.getNumberOfAccounts();
        defaultAccountStatus = count <= 1;
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
