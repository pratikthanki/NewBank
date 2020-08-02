package newbank.database;

import newbank.server.Account;
import newbank.server.Customer;
import newbank.server.CustomerID;

import java.util.*;

public class MapDatabaseClient implements DatabaseClient {
    HashMap<String, Customer> customers;

    public MapDatabaseClient() {
        customers = new HashMap<>();
        customers = addTestData();
    }

    private HashMap<String, Customer> addTestData() {
        HashMap<String, Customer> customers = new HashMap<>();
        String password = "MyPa55w0rd";

        Customer bhagy = new Customer();
        bhagy.setPassword(password);
        bhagy.addAccount(new Account("Main", 1000.0, 1234));
        bhagy.updateDetail("Baby Hagy", new GregorianCalendar(1982, Calendar.DECEMBER, 20).getTime(), "bhagy@bath.ac.uk", "Bath, London");
        customers.put("Bhagy", bhagy);

        Customer christina = new Customer();
        christina.setPassword(password);
        christina.addAccount(new Account("Savings", 1500.0, 2580));
        christina.updateDetail("Christina Aguilera", new GregorianCalendar(1985, Calendar.JANUARY, 11).getTime(), "christina.aguilera@celebrity.com", "Houston, USA");
        customers.put("Christina", christina);

        Customer john = new Customer();
        john.setPassword(password);
        john.addAccount(new Account("Checking", 250.0, 9876));
        john.addAccount(new Account("Savings", 50.0, 3490));
        john.updateDetail("John Doe", new Date(), "john.doe@newbank.com", "Lagos, Nigeria");
        customers.put("John", john);

        return customers;
    }

    public Collection<Customer> getCustomers() {
        return customers.values();
    }

    @Override
    public Customer getCustomerById(String customerId) {
        return customers.get(customerId);
    }

    @Override
    public void addNewCustomer(Customer customer) {
        customers.put(customer.getCustomerID().getKey(), customer);
    }

    @Override
    public boolean hasCustomer(String key) {
        return customers.containsKey(key);
    }

    @Override
    public void addAccount(CustomerID customerID, Account account) {
        customers.get(customerID.getKey()).addAccount(account);
    }

    @Override
    public List<Account> getAccounts(String customer) {
        return customers.get(customer).getAccounts();
    }

    @Override
    public String getAccountsAsString(String customer) {
        return customers.get(customer).accountsToString();
    }

    @Override
    public void updateCustomer(Customer customer) {
    }
}
