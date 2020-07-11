package newbank.server;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewBankTest {
    NewBank newBank;
    CustomerID customerID;

    @Before
    public void setUp() {
        newBank = NewBank.getBank();
        customerID = new CustomerID("John");
    }

    @Test
    public void getBank() {
        assertEquals(newBank, NewBank.getBank());

    }

    @Test
    public void checkLogInDetailsFail() {
        String username = "someUsername";
        String password = "somePassword";
        assertNull(newBank.checkLogInDetails(username, password));
    }

    @Test
    public void checkLogInDetailsSuccess() {
        String username = "John";
        String password = "somePassword";
        try{
            newBank.checkLogInDetails(username, password);
            assertTrue(true);
        } catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    public void processRequestFail() {
        String expected = "FAIL";
        assertEquals(expected, newBank.processRequest(customerID, "VIEWCASHBACK"));
    }

    @Test
    public void processRequestSuccess() {
        String expected = "Checking: 250.0";
        assertEquals(expected, newBank.processRequest(customerID, "SHOWMYACCOUNTS"));
    }
}
