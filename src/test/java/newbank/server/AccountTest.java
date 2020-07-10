package newbank.server;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
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
}
