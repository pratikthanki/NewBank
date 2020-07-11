package newbank.server;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CustomerTest extends TestCase {
    Account accountName;
    Customer customerName;

    @Before
    public void setUp() {
        accountName = new Account("savings", 123);
        customerName = new Customer();
        customerName.addAccount(accountName);
    }

    @Test
    public void testAccountsToString() {
        Assert.assertEquals("savings: 123.0", customerName.accountsToString());
    }

    @Test
    public void testAddAccount() {
        try {
            Account isa = new Account("isa", 123);
            customerName.addAccount(isa);

            Assert.assertTrue(true);
            Assert.assertNotNull(customerName);
        }
        catch(Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetAllAccounts(){
        List<Account> accountList = customerName.getAccounts();

        assertEquals(1, accountList.size());
        assertEquals("savings", accountList.get(0).getAccountName());
        assertEquals(123.0, accountList.get(0).getOpeningBalance());
    }
}
