package newbank.server.accounts;

//import com.sun.jdi.request.InvalidRequestStateException;
import newbank.server.NewBank;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CreditCardAccountTest {

    CreditCardAccount creditCardAccount;
    NewBank newBank;

    @Before
    public void SetUp() {
        newBank = NewBank.getBank();
    }

    @Test
    public void testSavingsAccountCreation() {
        creditCardAccount = new CreditCardAccount("Credit Card", 100.0, 1234, 1.5);
        assertEquals("Credit Card", creditCardAccount.getAccountName());
        assertEquals(100.0, creditCardAccount.getBalance(), 0.1);
        assertEquals(100.0, creditCardAccount.getCreditLimit(), 0.1);
        assertEquals(1.5, creditCardAccount.getInterestRate(), 0.1);
        LocalDateTime localDateTime = LocalDateTime.now();
        assertEquals(localDateTime.getDayOfMonth(), creditCardAccount.getStartDate().getDayOfMonth());
        assertEquals(localDateTime.getDayOfMonth(), creditCardAccount.getInterestPayableDate().getDayOfMonth());
    }

    @Test
    public void testPurchaseOnCredit() {
        creditCardAccount = new CreditCardAccount("Credit Card", 100.0, 1234, 1.5);

        double balance = creditCardAccount.purchaseOnCredit(50);

        assertEquals(50, balance, 0.1);
    }

    @Test
    public void testPurchaseOnCreditInvalid() {
        creditCardAccount = new CreditCardAccount("Credit Card", 100.0, 1234, 1.5);

        try {
            creditCardAccount.purchaseOnCredit(150);
            fail("Should have failed, as the cost of the purchase exceeds the credit available.");
        } catch(Exception e){
            assertEquals("Invalid request, not sufficient balance to make this purchase.", e.getMessage());
        }
    }

    @Test
    public void testAccrueInterest() {
        creditCardAccount = new CreditCardAccount("Credit Card", 100.0, 1234, 1.5);

        creditCardAccount.purchaseOnCredit(10);

        LocalDateTime localDateTime = LocalDateTime.now().plusMonths(1);

        double balance = creditCardAccount.accrueInterest(localDateTime);

        assertEquals(85.0, balance, 0.1);
    }
}
