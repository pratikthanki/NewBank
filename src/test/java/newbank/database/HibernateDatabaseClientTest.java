package newbank.database;

import newbank.server.Account;
import newbank.server.Customer;
import newbank.server.CustomerID;
import newbank.server.accounts.Savings;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class HibernateDatabaseClientTest {

    private HibernateDatabaseClient hibernateDatabaseClient;

    @Before
    public void setUp(){
        hibernateDatabaseClient = new HibernateDatabaseClient(HibernateUtility.test());
    }

    @Test
    public void getAllCustomers(){
        String customerId = "testUser";
        Customer testUser = new Customer(new CustomerID(customerId));
        hibernateDatabaseClient.addNewCustomer(testUser);

        List<Customer> actualCustomers = new ArrayList<>(hibernateDatabaseClient.getCustomers());
        assertEquals(1, actualCustomers.size());
        assertEquals(customerId, actualCustomers.get(0).getCustomerID().getKey());

    }

    @Test
    public void canAddAccount(){
        String customerId = "testUser";
        Customer testUser = new Customer(new CustomerID(customerId));
        hibernateDatabaseClient.addNewCustomer(testUser);

        Customer customer = hibernateDatabaseClient.getCustomerById(customerId);

        assertEquals(0, customer.getNumberOfAccounts());

        hibernateDatabaseClient.addAccount(customer.getCustomerID(), new Savings("Main", 0.0,
                1, 0, 0));

        Customer customerAfterAccountHasBeenAdded = hibernateDatabaseClient.getCustomerById(customerId);

        assertEquals(1, customerAfterAccountHasBeenAdded.getNumberOfAccounts());
    }
}

