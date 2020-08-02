package newbank.server.accounts;

import newbank.exceptions.InsufficientFundsException;
import newbank.server.Account;

import java.time.LocalDateTime;

public class CreditCardAccount extends Account {

    private double interestRate;
    private LocalDateTime startDate;
    private LocalDateTime interestPayableDate;
    private double creditLimit;

    public CreditCardAccount(String accountName, double openingBalance, int accountPin, double interestRate) {
        super(accountName, openingBalance, accountPin);
        this.startDate = LocalDateTime.now();
        this.interestPayableDate = LocalDateTime.now();
        this.creditLimit = openingBalance;
        this.interestRate = interestRate;
    }

    public double purchaseOnCredit(double price) throws InsufficientFundsException {
        if (getBalance() > price) {
            addMoney(-price);
        } else {
            throw new InsufficientFundsException("Invalid request, not sufficient balance to make this purchase.");
        }
        return getBalance();
    }

    public double accrueInterest(LocalDateTime currentDateTime) {
        int dayOfMonth = interestPayableDate.getDayOfMonth();
        if (currentDateTime.getDayOfMonth() == dayOfMonth && !currentDateTime.equals(interestPayableDate)) {
            double balance = getBalance();
            double moneyOwed = creditLimit - balance;
            double interest = (moneyOwed * interestRate) - moneyOwed;
            addMoney(-interest);
        }
        return getBalance();
    }

    public double getInterestRate() {
        return interestRate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getInterestPayableDate() {
        return interestPayableDate;
    }

    public double getCreditLimit() {
        return creditLimit;
    }
}
