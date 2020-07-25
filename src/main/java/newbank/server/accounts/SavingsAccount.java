package newbank.server.accounts;

import newbank.server.Account;
import newbank.server.CustomerID;
import newbank.server.NewBank;

import java.time.LocalDateTime;

public class SavingsAccount extends Account {

    private double interestRate;
    private LocalDateTime startDate;
    private LocalDateTime interestPayableDate;
    private double standingOrderAmount;
    private NewBank newBank;

    public SavingsAccount(CustomerID customer, Account primaryAccount, int accountPin, String accountName, double openingBalance, double standingOrderAmount) {
        super(accountName, openingBalance, accountPin);
        startDate = LocalDateTime.now();
        setUpStandingOrderBetweenAccounts(customer, primaryAccount, accountName, standingOrderAmount);
    }

    private void setUpStandingOrderBetweenAccounts(CustomerID customer, Account primaryAccount, String accountName, double standingOrderAmount){
        newBank = NewBank.getBank();
        int dayOfMonth = startDate.getDayOfMonth();
        if(LocalDateTime.now().getDayOfMonth() == dayOfMonth && !LocalDateTime.now().equals(startDate)){
            newBank.processRequest(customer, "MOVE " + standingOrderAmount + " " + primaryAccount.getAccountName() + " " + accountName);
        }
    }
}
