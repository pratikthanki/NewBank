package newbank.server;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private int id;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "balance")
    private double balance;

    @Column(name = "default_account_status")
    private boolean defaultAccountStatus;

    @Column(name = "account_pin")
    private int accountPin;

    public Account(){}

    public Account(String accountName, double openingBalance, int accountPin) {
        this.accountName = accountName;
        this.balance = openingBalance;
        this.defaultAccountStatus = false;
        this.accountPin = accountPin;
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

    public void addMoney(double money) {
        balance += money;
    }

    public void withdrawMoney(double money) {
        balance -= money;
    }

    public double getBalance() {
        return balance;
    }

    public int getId(){
        return id;
    }

    public String toString() {
        return (accountName + ": " + balance);
    }

}
