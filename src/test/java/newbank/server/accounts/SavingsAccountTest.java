package newbank.server.accounts;

import newbank.server.Account;
import newbank.server.Customer;
import newbank.server.CustomerID;
import newbank.server.NewBank;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class SavingsAccountTest {

    SavingsAccount savingsAccount;
    NewBank newBank;

    @Before
    public void SetUp() {
        newBank = NewBank.getBank();
    }

    @Test
    public void testSavingsAccountCreation() {
        savingsAccount = new SavingsAccount("Savings", 100.0, 1234, 50.0, 1.5);
        assertEquals("Savings", savingsAccount.getAccountName());
        assertEquals(100.0, savingsAccount.getBalance(), 0.1);
        assertEquals(50, savingsAccount.getStandingOrderAmount(), 0.1);
        assertEquals(1.5, savingsAccount.getInterestRate(), 0.1);
        LocalDateTime localDateTime = LocalDateTime.now();
        assertEquals(localDateTime.getDayOfMonth(), savingsAccount.getStartDate().getDayOfMonth());
        assertEquals(localDateTime.getDayOfMonth(), savingsAccount.getInterestPayableDate().getDayOfMonth());
    }

    @Test
    public void testInterestDeposit() {
        savingsAccount = new SavingsAccount("Savings", 100.0, 1234, 50.0, 1.5);

        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(1);
        double balance = savingsAccount.payInterestIntoAccount(localDateTime);

        assertEquals(150, balance, 0.1);
    }


    @Test
    public void testStandingOrderDeposit() {
        savingsAccount = new SavingsAccount("Savings", 100.0, 1234, 50.0, 1.5);
        Account checking = new Account("Checking", 123, 1234);

        Customer customer = new Customer(new CustomerID("John"));
        customer.addAccount(savingsAccount);
        customer.addAccount(checking);
        checking.setDefaultAccount(customer);

        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(1);

        savingsAccount.setUpStandingOrderBetweenAccounts(customer, checking,50, localDateTime);

        assertEquals(150.0, savingsAccount.getBalance(), 0.1);
    }
}
