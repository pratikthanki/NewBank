package newbank.server;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewBankTest {
    NewBank newBank = NewBank.getBank();
    CustomerID customerID = new CustomerID("John");
    CustomerID bhagy = new CustomerID("Bhagy");

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
        String password = "MyPa55w0rd";
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
        String expected = "Main: 1000.0";
        assertEquals(expected, newBank.processRequest(bhagy, "SHOWMYACCOUNTS"));
    }

    @Test
    public void moveMoneyBetweenAccountsSuccess(){
        String result = newBank.processRequest(customerID, "MOVE 50 Checking Savings");

        assertEquals("SUCCESS", result);
    }

    @Test
    public void moveMoneyBetweenAccountsFailed(){
        String missingArgument = newBank.processRequest(customerID, "MOVE 100 Checking");

        assertEquals("FAIL", missingArgument);

        String invalidAmount = newBank.processRequest(customerID, "MOVE 1000 Checking Savings");

        assertEquals("FAIL", invalidAmount);

        String invalidFromAccount = newBank.processRequest(customerID, "MOVE 50 Check Savings");

        assertEquals("FAIL", invalidFromAccount);

        String invalidToAccount = newBank.processRequest(customerID, "MOVE 50 Checking Save");

        assertEquals("FAIL", invalidToAccount);
    }

    @Test
    public void createNewAccount(){
        String newAccountName = "Savings";
        String expectedResult = "SUCCESS";

        assertEquals(expectedResult, newBank.processRequest(customerID, "NEWACCOUNT "+newAccountName));
        assertTrue(newBank.processRequest(customerID, "SHOWMYACCOUNTS").contains(newAccountName+": 0.0"));

    }

    @Test
    public void createNewAccountWithoutAccountName(){
        assertEquals("FAIL", newBank.processRequest(customerID, "NEWACCOUNT"));
    }
}
