package newbank.server;

import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
    
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
        
        customer.setEmail("test@gmail.com");
        customer.setAddress("6 Osapa, Lagos, Nigeria");
        customer.setName("Peter Test");
        
        try {
        	customer.setDob(new SimpleDateFormat("d M yyyy").parse("2 8 1912"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testAccountsToString() {
        assertEquals("savings: 123.0", customer.accountsToString());
    }
    
    @Test
    public void testSetEmail() {
    	assertEquals("test@gmail.com", customer.getEmail());
    }
    
    @Test
    public void testSetAddress() {
        assertEquals("6 Osapa, Lagos, Nigeria", customer.getAddress());
    }
    
    @Test
    public void testSetName() {
       assertEquals("Peter Test", customer.getName());
    }
    
    @Test
    public void testSetDob() {
        try {
			assertEquals(new SimpleDateFormat("d M yyyy").parse("2 8 1912"), customer.getDob());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
