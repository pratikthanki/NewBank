package newbank.server;

import newbank.server.StaticData.AccountsData;
import org.junit.*;

import static org.junit.Assert.*;

import java.util.HashMap;

public class NewBankTest {
    NewBank newBank;
    CustomerID john;
    CustomerID bhagy;

    @Before
    public void SetUp() {
        newBank = NewBank.getBank();
        john = new CustomerID("John");
        bhagy = new CustomerID("Bhagy");
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
        String password = "MyPa55w0rd";
        try{
            newBank.checkLogInDetails(username, password);
            assertTrue(true);
        } catch (Exception e){
            fail();
        }
    }

    @Test
    public void processRequestFail() {
        String expected = "FAIL";
        assertEquals(expected, newBank.processRequest(john, "VIEWCASHBACK"));
    }

    @Test
    public void processRequestSuccess() {
        String expected = "Main: 1000.0";
        assertEquals(expected, newBank.processRequest(bhagy, AccountsData.SHOWMYACCOUNTS));
    }

    @Test
    public void moveMoneyBetweenAccountsSuccess(){
        String result = newBank.processRequest(john, "MOVE 50 Checking Savings");

        assertEquals(AccountsData.success, result);
    }

    @Test
    public void moveMoneyBetweenAccountsFailed() {
        String missingArgument = newBank.processRequest(john, "MOVE 100 Checking");
        assertEquals(AccountsData.fail, missingArgument);

        String invalidAmount = newBank.processRequest(john, "MOVE 1000 Checking Savings");
        assertEquals(AccountsData.success, invalidAmount);

        String invalidFromAccount = newBank.processRequest(john, "MOVE 50 Check Savings");
        assertEquals(AccountsData.fail, invalidFromAccount);

        String invalidToAccount = newBank.processRequest(john, "MOVE 50 Checking Save");
        assertEquals(AccountsData.fail, invalidToAccount);
    }

    @Test
    public void payMoneyBetweenCustomers(){
        String argument = newBank.processRequest(bhagy, "PAY John 100 Main");
        assertEquals(AccountsData.a900, newBank.processRequest(bhagy, AccountsData.SHOWMYACCOUNTS).substring(6));
        assertEquals(AccountsData.a350, newBank.processRequest(john, AccountsData.SHOWMYACCOUNTS).substring(10, 15));
        assertEquals(AccountsData.success, argument);

        String argument1 = newBank.processRequest(john, "PAY Bhagy 100 Checking");
        assertEquals(AccountsData.a1000, newBank.processRequest(bhagy, AccountsData.SHOWMYACCOUNTS).substring(6));
        assertEquals(AccountsData.a250, newBank.processRequest(john, AccountsData.SHOWMYACCOUNTS).substring(10, 15));
        assertEquals(AccountsData.success, argument1);
    }

    @Test
    public void payMoneyBetweenCustomersFailed(){
        //Customer does not exist
        String argument = newBank.processRequest(bhagy, "PAY test 100 Main");
        assertEquals(AccountsData.a1000, newBank.processRequest(bhagy, AccountsData.SHOWMYACCOUNTS).substring(6));
        assertEquals(AccountsData.a250, newBank.processRequest(john, AccountsData.SHOWMYACCOUNTS).substring(10, 15));
        assertEquals(AccountsData.fail, argument);

        //Customer account does not exist
        String argument1 = newBank.processRequest(bhagy, "PAY John 100 Main1");
        assertEquals(AccountsData.a1000, newBank.processRequest(bhagy, AccountsData.SHOWMYACCOUNTS).substring(6));
        assertEquals(AccountsData.a250, newBank.processRequest(john, AccountsData.SHOWMYACCOUNTS).substring(10, 15));
        assertEquals(AccountsData.fail, argument1);

        //Invalid amount
        String argument2 = newBank.processRequest(bhagy, "PAY John 100a Main");
        assertEquals(AccountsData.a1000, newBank.processRequest(bhagy, AccountsData.SHOWMYACCOUNTS).substring(6));
        assertEquals(AccountsData.a250, newBank.processRequest(john, AccountsData.SHOWMYACCOUNTS).substring(10, 15));
        assertEquals(AccountsData.fail, argument2);
    }

    @Test
    public void createNewAccount(){
        String newAccountName = "Savings";
        String expectedResult = "SUCCESS";

        assertEquals(expectedResult, newBank.processRequest(john, AccountsData.NEWACCOUNT + " " + newAccountName));
        assertTrue(newBank.processRequest(john, AccountsData.SHOWMYACCOUNTS).contains(newAccountName + ": 0.0"));
    }

    @Test
    public void createNewAccountWithoutAccountName(){
        assertEquals(AccountsData.fail, newBank.processRequest(john, AccountsData.NEWACCOUNT));
    }
    
    public void registerNewCustomer() {
    	HashMap<String, String> properties = new HashMap<>();
		
		properties.put("firstname", "Peter");
		properties.put("surname", "Pan");
		properties.put("email", "peter.pan@gmail.com");
		properties.put("address", "Lagos, Nigeria");
		properties.put("dob", "5 7 2000");
		properties.put("password1", "MyPa55w0rd");
		properties.put("password2", "MyPa55w0rd");
		newBank.registerNewCustomer(properties);
        assertTrue(true);
    }
}
