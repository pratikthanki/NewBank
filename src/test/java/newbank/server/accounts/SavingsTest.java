package newbank.server.accounts;

import newbank.server.Account;
import newbank.server.Customer;
import newbank.server.CustomerID;
import newbank.server.NewBank;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class SavingsTest {

    Savings savings;
    NewBank newBank;

    @Before
    public void SetUp() {
        newBank = NewBank.getBank();
    }

    @Test
    public void testSavingsAccountCreation() {
        savings = new Savings("Savings", 100.0, 1234, 50.0, 1.5);
        assertEquals("Savings", savings.getAccountName());
        assertEquals(100.0, savings.getBalance(), 0.1);
        assertEquals(50, savings.getStandingOrderAmount(), 0.1);
        assertEquals(1.5, savings.getInterestRate(), 0.1);
        LocalDateTime localDateTime = LocalDateTime.now();
        assertEquals(localDateTime.getDayOfMonth(), savings.getStartDate().getDayOfMonth());
        assertEquals(localDateTime.getDayOfMonth(), savings.getInterestPayableDate().getDayOfMonth());
    }

    @Test
    public void testInterestDeposit() {
        savings = new Savings("Savings", 100.0, 1234, 50.0, 1.5);

        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(1);
        double balance = savings.payInterestIntoAccount(localDateTime);

        assertEquals(150, balance, 0.1);
    }


    @Test
    public void testStandingOrderDeposit() {
        savings = new Savings("Savings", 100.0, 1234, 50.0, 1.5);
        Account checking = new Account("Checking", 123, 1234);

        Customer customer = new Customer(new CustomerID("John"));
        customer.addAccount(savings);
        customer.addAccount(checking);
        checking.setDefaultAccount(customer);

        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(1);

        savings.setUpStandingOrderBetweenAccounts(customer, checking,50, localDateTime);

        assertEquals(150.0, savings.getBalance(), 0.1);
    }
}
