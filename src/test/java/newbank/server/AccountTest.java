package newbank.server;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountTest {
    Account accountName;
    NewBank newBank;

    @Before
    public void SetUp() {
        newBank = NewBank.getBank();

    }
    @Test
    public void testAddAccount_AllowNull() {
        accountName = new Account(null, 123, 1234);
        assertEquals("null: 123.0", accountName.toString());
    }

    @Test
    public void testAddAccount() {
        accountName = new Account("ISA", 123.45, 1234);
        assertEquals("ISA: 123.45", accountName.toString());
    }

    @Test
    public void testToString() {
        accountName = new Account("savings", 123, 1234);
        assertEquals("savings: 123.0", accountName.toString());
    }

    @Test
    public void testGetAndSetAccountNameAndOpeningBalance() {
        accountName = new Account("Checking", 123, 1234);

        assertEquals("Checking", accountName.getAccountName());
        Assert.assertEquals(123.0, accountName.getBalance(), 0);

        accountName.addMoney(10.0);
        accountName.setAccountName("Savings");

        assertEquals("Savings", accountName.getAccountName());
        assertEquals(133.0, accountName.getBalance(),0);

        accountName.withdrawMoney(100.0);

        assertEquals(33.0, accountName.getBalance(),0);
    }


}
