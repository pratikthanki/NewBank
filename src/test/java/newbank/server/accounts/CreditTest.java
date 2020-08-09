package newbank.server.accounts;

import newbank.exceptions.InsufficientFundsException;
import newbank.server.Account;
import newbank.server.Customer;
import newbank.server.CustomerID;
import newbank.server.NewBank;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CreditTest {

    Credit credit;
    NewBank newBank;

    @Before
    public void SetUp() {
        newBank = NewBank.getBank();
    }

    @Test
    public void testSavingsAccountCreation() {
        credit = new Credit("Credit Card", 100.0, 1234, 1.5);
        assertEquals("Credit Card", credit.getAccountName());
        assertEquals(100.0, credit.getBalance(), 0.1);
        assertEquals(100.0, credit.getCreditLimit(), 0.1);
        assertEquals(1.5, credit.getInterestRate(), 0.1);
        LocalDateTime localDateTime = LocalDateTime.now();
        assertEquals(localDateTime.getDayOfMonth(), credit.getStartDate().getDayOfMonth());
        assertEquals(localDateTime.getDayOfMonth(), credit.getInterestPayableDate().getDayOfMonth());
    }

    @Test
    public void testPurchaseOnCredit() throws InsufficientFundsException {
        credit = new Credit("Credit Card", 100.0, 1234, 1.5);

        double balance = credit.purchaseOnCredit(50);

        assertEquals(50, balance, 0.1);
    }

    @Test
    public void testPurchaseOnCreditInvalid() {
        credit = new Credit("Credit Card", 100.0, 1234, 1.5);

        try {
            credit.purchaseOnCredit(150);
            fail("Should have failed, as the cost of the purchase exceeds the credit available.");
        } catch(InsufficientFundsException e){
            assertEquals("Invalid request, not sufficient balance to make this purchase.", e.getMessage());
        }
    }

    @Test
    public void testAccrueInterest() throws InsufficientFundsException {
        credit = new Credit("Credit Card", 100.0, 1234, 1.5);

        credit.purchaseOnCredit(10);

        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(1);

        double balance = credit.accrueInterest(localDateTime);

        assertEquals(85.0, balance, 0.1);
    }

    @Test
    public void payOffCreditCardBalance() throws InsufficientFundsException {
        credit = new Credit("Credit Card", 100.0, 1234, 1.15);
        Account checking = new Account("Checking", 123, 1234);

        credit.purchaseOnCredit(10);

        assertEquals(90, credit.getBalance(), 0.1);

        Customer customer = new Customer(new CustomerID("John"));
        customer.addAccount(credit);
        customer.addAccount(checking);

        credit.payOffCreditCardBalance(customer, checking, 10.0);

        assertEquals(100, credit.getBalance(), 0.1);

    }

    @Test
    public void payOffFullDebtCreditCardBalance() throws InsufficientFundsException {
        credit = new Credit("Credit Card", 100.0, 1234, 1.15);
        Account checking = new Account("Checking", 123, 1234);

        credit.purchaseOnCredit(10);

        assertEquals(90, credit.getBalance(), 0.1);

        Customer customer = new Customer(new CustomerID("John"));
        customer.addAccount(credit);
        customer.addAccount(checking);
        customer.setDefaultAccount(checking);

        credit.payOffFullDebtFromDefaultAccount(customer);

        assertEquals(100, credit.getBalance(), 0.1);
    }
}
