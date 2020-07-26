package newbank.server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {
    Account savings;
    Account isa;
    Customer customer;

    @Before
    public void setUp() {
        savings = new Account("savings", 123, 1234);
        customer = new Customer();
        customer.addAccount(savings);
    }

    @Test
    public void testAccountsToString() {
        assertEquals("savings: 123.0", customer.accountsToString());
    }

    @Test
    public void testAddAccount() {
        try {
            Account checkings = new Account("checkings", 123, 1234);
            assertTrue(true);
            assertFalse(checkings.isDefaultAccount());
        }
        catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testAddTwoAccounts() {
        try {
            isa = new Account("isa", 123, 1234);
            assertTrue(true);
            assertTrue(savings.isDefaultAccount());
            assertFalse(isa.isDefaultAccount());

        }
        catch(Exception e) {
            fail();
        }
    }

    @Test
    public void testGetAllAccounts(){
        assertEquals(1, customer.getNumberOfAccounts());
        assertEquals("savings", savings.getAccountName());
        assertEquals(123.0, savings.getBalance(),0);
    }
}
