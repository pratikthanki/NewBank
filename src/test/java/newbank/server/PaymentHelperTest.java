package newbank.server;

import newbank.server.StaticData.AccountsData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentHelperTest {
    Account account;
    Account account1;
    NewBank newBank;
    PaymentHelper paymentHelper;

    @Before
    public void SetUp() {
        newBank = NewBank.getBank();
        paymentHelper = new PaymentHelper();
    }

    @Test
    public void testGetDefaultAccount(){
        Customer john = new Customer();
        john.addAccount(new Account(AccountsData.checking, 250.0), false);
        john.addAccount(new Account(AccountsData.savings, 50.0), true);
        john.addAccount(new Account(AccountsData.isa, 50.0), false);
        assertEquals(john.getAccounts().get(1), paymentHelper.getDefaultAccount(john));
    }

    @Test
    public void testCalculateTransaction() {
         account = new Account(AccountsData.isa, 100.0);
         account1 = new Account(AccountsData.savings, 100.0);
         paymentHelper.calculateTransaction(account ,account1,50.0);
         assertEquals(AccountsData.isa50, account.toString());
         assertEquals(AccountsData.savings150, account1.toString());

    }
}
