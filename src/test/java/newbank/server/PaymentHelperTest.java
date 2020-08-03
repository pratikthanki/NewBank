package newbank.server;

import newbank.server.StaticData.AccountsData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentHelperTest {
    Account account;
    Account account1;
    IPaymentHelper paymentHelper;

    @Before
    public void SetUp() {
        paymentHelper = new IPaymentHelper();
    }

    @Test
    public void testGetDefaultAccount() {
        Customer john = new Customer();
        john.addAccount(new Account(AccountsData.checking, 250.0, 1234));
        john.addAccount(new Account(AccountsData.savings, 50.0, 1234));
        john.addAccount(new Account(AccountsData.isa, 50.0, 1234));
        assertEquals(john.getAccounts().get(0), john.getDefaultAccount());
        assertEquals(false, john.getAccounts().get(1).isDefaultAccount());
        assertEquals(false, john.getAccounts().get(2).isDefaultAccount());
    }

    @Test
    public void testCalculateTransaction() {
        account = new Account(AccountsData.isa, 100.0, 1234);
        account1 = new Account(AccountsData.savings, 100.0, 1234);
        paymentHelper.calculateTransaction(account, account1, 50.0);
        assertEquals(AccountsData.isa50, account.toString());
        assertEquals(AccountsData.savings150, account1.toString());

    }
}
