package newbank.server;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CustomerTest extends TestCase {
    Account accountName;
    Customer customerName;
    

    @Before
    public void setUp() {
        accountName = new Account("savings", 123);
        customerName = new Customer();
        customerName.addAccount(accountName);
        
        customerName.setEmail("test@gmail.com");
        customerName.setAddress("6 Osapa, Lagos, Nigeria");
        customerName.setName("Peter Test");
        try {
			customerName.setDob(new SimpleDateFormat("d M yyyy").parse("2 8 1912"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
    }

    @Test
    public void testAccountsToString() {
        Assert.assertEquals("savings: 123.0", customerName.accountsToString());
    }
    
    @Test
    public void testSetEmail() {
        Assert.assertEquals("test@gmail.com", customerName.getEmail());
    }
    
    @Test
    public void testSetAddress() {
        Assert.assertEquals("6 Osapa, Lagos, Nigeria", customerName.getAddress());
    }
    
    @Test
    public void testSetName() {
        Assert.assertEquals("Peter Test", customerName.getName());
    }
    
    @Test
    public void testSetDob() {
        try {
			Assert.assertEquals(new SimpleDateFormat("d M yyyy").parse("2 8 1912"), customerName.getDob());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
