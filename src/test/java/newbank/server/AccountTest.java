package newbank.server;

import junit.framework.TestCase;
import org.junit.Test;

public class AccountTest extends TestCase {
    Account accountName;

    @Test
    public void testAddAccount_AllowNull() {
        accountName = new Account(null, 123);
        assertEquals("null: 123.0", accountName.toString());
    }

    @Test
    public void testAddAccount() {
        accountName = new Account("ISA", 123.45);
        assertEquals("ISA: 123.45", accountName.toString());
    }

    @Test
    public void testToString() {
        accountName = new Account("savings", 123);
        assertEquals("savings: 123.0", accountName.toString());
    }

    @Test
    public void testGetAndSetAccountNameAndOpeningBalance(){
        accountName = new Account("Checking", 123);

        assertEquals("Checking", accountName.getAccountName());
        assertEquals(123.0, accountName.getOpeningBalance());

        accountName.setOpeningBalance(234.0);
        accountName.setAccountName("Savings");

        assertEquals("Savings", accountName.getAccountName());
        assertEquals(234.0, accountName.getOpeningBalance());
    }
}
