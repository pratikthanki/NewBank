package newbank.server.accounts;

import newbank.exceptions.InsufficientFundsException;
import newbank.server.Account;
import newbank.server.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Credit extends Account {

    private double interestRate;
    private LocalDateTime startDate;
    private LocalDateTime interestPayableDate;
    private double creditLimit;

    public Credit(String accountName, double openingBalance, int accountPin, double interestRate) {
        super(accountName, openingBalance, accountPin);
        this.startDate = LocalDateTime.now();
        this.interestPayableDate = LocalDateTime.now();
        this.creditLimit = openingBalance;
        this.interestRate = interestRate;
    }

    public double purchaseOnCredit(double price) throws InsufficientFundsException {
        if (getBalance() > price) {
            withdrawMoney(price);
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
            withdrawMoney(interest);
        }
        return getBalance();
    }

    public void payOffCreditCardBalance(Customer customer, Account account, double amountToPay){
        List<String> accountNames = customer.getAccounts().stream().map(Account::getAccountName).collect(Collectors.toList());
        if(accountNames.contains(account.getAccountName()) && accountNames.contains(getAccountName())){
            account.withdrawMoney(amountToPay);
            addMoney(amountToPay);
        }
    }

    public void payOffFullDebtFromDefaultAccount(Customer customer){
        Account account = customer.getDefaultAccount();
        Double amountToPay = creditLimit - getBalance();
        account.withdrawMoney(amountToPay);
        addMoney(amountToPay);
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
