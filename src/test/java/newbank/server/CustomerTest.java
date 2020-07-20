package newbank.server;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CustomerTest {
    Account accountName;
    Customer customerName;

    @Before
    public void setUp() {
        accountName = new Account("savings", 123);
        customerName = new Customer();
        customerName.addAccount(accountName, true);
    }

    @Test
    public void testAccountsToString() {
        assertEquals("savings: 123.0", customerName.accountsToString());
    }

    @Test
    public void testAddAccount() {
        try {
            Account isa = new Account("isa", 123);
            customerName.addAccount(isa, true);

            assertTrue(true);
            assertNotNull(customerName);
        }
        catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testGetAllAccounts(){
        List<Account> accountList = customerName.getAccounts();

        assertEquals(1, accountList.size());
        assertEquals("savings", accountList.get(0).getAccountName());
        assertEquals(123.0, accountList.get(0).getBalance(),0);
    }
}
