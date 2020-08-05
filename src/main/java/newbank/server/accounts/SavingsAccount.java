package newbank.server.accounts;

import newbank.server.Account;
import newbank.server.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SavingsAccount extends Account {

    private double interestRate;
    private LocalDateTime startDate;
    private LocalDateTime interestPayableDate;
    private double standingOrderAmount;

    public SavingsAccount(String accountName, double openingBalance, int accountPin, double standingOrderAmount, double interestRate) {
        super(accountName, openingBalance, accountPin);
        this.startDate = LocalDateTime.now();
        this.interestPayableDate = LocalDateTime.now();
        this.interestRate = interestRate;
        this.standingOrderAmount = standingOrderAmount;
    }


    // have added this method, but may need to look into running a daily job to check the current date and if the money should be transferred over the savings
    public void setUpStandingOrderBetweenAccounts(Customer customer, Account account, double standingOrderAmount, LocalDateTime currentDateTime){
        int dayOfMonth = startDate.getDayOfMonth();
        if(currentDateTime.getDayOfMonth() == dayOfMonth && !currentDateTime.equals(startDate)){
            List<String> accountNames = customer.getAccounts().stream().map(Account::getAccountName).collect(Collectors.toList());
            if(accountNames.contains(account.getAccountName()) && accountNames.contains(getAccountName())){
                account.withdrawMoney(standingOrderAmount);
                addMoney(standingOrderAmount);
            }
        }
    }

    public double payInterestIntoAccount(LocalDateTime currentDateTime){
        int dayOfMonth = interestPayableDate.getDayOfMonth();
        if(currentDateTime.getDayOfMonth() == dayOfMonth && !currentDateTime.equals(interestPayableDate)){
            double balance = getBalance();
            double newBalance = balance * interestRate;
            double addMoney = newBalance - balance;
            addMoney(addMoney);
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

    public double getStandingOrderAmount() {
        return standingOrderAmount;
    }
}
