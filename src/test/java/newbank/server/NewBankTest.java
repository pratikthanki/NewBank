package newbank.server;

import newbank.server.StaticData.AccountsData;
import org.junit.*;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

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
        assertEquals(expected, newBank.processRequest(john, Command.VIEWCASHBACK));
    }

    @Test
    public void processRequestSuccess() {
        String expected = "Main: 1000.0";
        assertEquals(expected, newBank.processRequest(bhagy, Command.SHOWMYACCOUNTS));
    }

    @Test
    public void moveMoneyBetweenAccountsSuccess(){
		Map<Parameter, String> properties = new HashMap<>();
		
		properties.put(Parameter.AMOUNT, "50");
		properties.put(Parameter.FROM_ACCOUNT, "Checking");
		properties.put(Parameter.TO_ACCOUNT, "Savings");
		String result = newBank.processRequest(john,Command.MOVE, properties);

        assertEquals(AccountsData.success, result);
    }

    @Test
    public void moveMoneyBetweenAccountsFailed(){
    	Map<Parameter, String> properties = new HashMap<>();
		
		properties.put(Parameter.AMOUNT, "50");
		properties.put(Parameter.FROM_ACCOUNT, "Checking0");
		
        String missingArgument = newBank.processRequest(john,Command.MOVE, properties);

        assertEquals(AccountsData.fail, missingArgument);
        
        properties.clear();
        properties.put(Parameter.AMOUNT, "1000");
		properties.put(Parameter.FROM_ACCOUNT, "Checking");
		properties.put(Parameter.TO_ACCOUNT, "Savings");
        String invalidAmount = newBank.processRequest(john,Command.MOVE, properties);

        assertEquals(AccountsData.fail, invalidAmount);
        
        properties.clear();
        properties.put(Parameter.AMOUNT, "50");
		properties.put(Parameter.FROM_ACCOUNT, "Check");
		properties.put(Parameter.TO_ACCOUNT, "Savings");
        String invalidFromAccount = newBank.processRequest(john,Command.MOVE, properties);

        assertEquals(AccountsData.fail, invalidFromAccount);
        
        properties.clear();
        properties.put(Parameter.AMOUNT, "50");
		properties.put(Parameter.FROM_ACCOUNT, "Checking");
		properties.put(Parameter.TO_ACCOUNT, "Save");
        String invalidToAccount = newBank.processRequest(john,Command.MOVE, properties);

        assertEquals(AccountsData.fail, invalidToAccount);
    }

    @Test
    public void payMoneyBetweenCustomers(){
    	Map<Parameter, String> properties = new HashMap<>();
    	
		properties.put(Parameter.AMOUNT, "100");
		properties.put(Parameter.PAYEE_CUSTOMER_NAME, "John");
		properties.put(Parameter.FROM_ACCOUNT, "Main");
		
        String argument = newBank.processRequest(bhagy,Command.PAY, properties);
        assertEquals(AccountsData.a900, newBank.processRequest(bhagy, Command.SHOWMYACCOUNTS, properties).substring(6));
        assertEquals(AccountsData.a350, newBank.processRequest(john, Command.SHOWMYACCOUNTS, properties).substring(10, 15));
        assertEquals(AccountsData.success, argument);
        
        properties.clear();
        properties.put(Parameter.AMOUNT, "100");
		properties.put(Parameter.PAYEE_CUSTOMER_NAME, "Bhagy");
		properties.put(Parameter.FROM_ACCOUNT, "Checking");
		
        String argument1 = newBank.processRequest(john,Command.PAY, properties);
        assertEquals(AccountsData.a1000, newBank.processRequest(john, Command.SHOWMYACCOUNTS, properties).substring(6));
        assertEquals(AccountsData.a250, newBank.processRequest(john, Command.SHOWMYACCOUNTS).substring(10, 15));
        assertEquals(AccountsData.success, argument1);
    }

    @Test
    public void payMoneyBetweenCustomersFailed(){
    	Map<Parameter, String> properties = new HashMap<>();
    	
		properties.put(Parameter.AMOUNT, "100");
		properties.put(Parameter.PAYEE_CUSTOMER_NAME, "test");
		properties.put(Parameter.FROM_ACCOUNT, "Main");
        //Customer does not exist
        String argument = newBank.processRequest(bhagy, Command.PAY, properties);
        assertEquals(AccountsData.a1000, newBank.processRequest(bhagy, Command.SHOWMYACCOUNTS).substring(6));
        assertEquals(AccountsData.a250, newBank.processRequest(john, Command.SHOWMYACCOUNTS).substring(10, 15));
        assertEquals(AccountsData.fail, argument);

        //Customer account does not exist
        properties.clear();
        properties.put(Parameter.AMOUNT, "100");
		properties.put(Parameter.PAYEE_CUSTOMER_NAME, "John");
		properties.put(Parameter.FROM_ACCOUNT, "Main1");
        String argument1 = newBank.processRequest(bhagy, Command.PAY, properties);
        assertEquals(AccountsData.a1000, newBank.processRequest(bhagy, Command.SHOWMYACCOUNTS).substring(6));
        assertEquals(AccountsData.a250, newBank.processRequest(john, Command.SHOWMYACCOUNTS).substring(10, 15));
        assertEquals(AccountsData.fail, argument1);

        //Invalid amount
        properties.clear();
        properties.put(Parameter.AMOUNT, "100a");
		properties.put(Parameter.PAYEE_CUSTOMER_NAME, "John");
		properties.put(Parameter.FROM_ACCOUNT, "Main");
        String argument2 = newBank.processRequest(bhagy, Command.PAY, properties); 
        assertEquals(AccountsData.a1000, newBank.processRequest(bhagy, Command.SHOWMYACCOUNTS).substring(6));
        assertEquals(AccountsData.a250, newBank.processRequest(john, Command.SHOWMYACCOUNTS).substring(10, 15));
        assertEquals(AccountsData.fail, argument2);
    }

    @Test
    public void createNewAccount(){
        String newAccountName = "Savings";
        String expectedResult = "SUCCESS";
        
        Map<Parameter, String> properties = new HashMap<>();
    	
		properties.put(Parameter.ACCOUNT_NAME, newAccountName);
        assertEquals(expectedResult, newBank.processRequest(john, Command.NEWACCOUNT, properties)); //newAccountName));
        assertTrue(newBank.processRequest(john, Command.SHOWMYACCOUNTS).contains(newAccountName + ": 0.0"));
    }

    @Test
    public void createNewAccountWithoutAccountName(){
        assertEquals(AccountsData.fail, newBank.processRequest(john, Command.NEWACCOUNT));
    }
}
