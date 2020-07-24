package newbank.database;

import newbank.server.Account;
import newbank.server.Customer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class DatabaseClient {
    HashMap<String, Customer> customers;

    public DatabaseClient() {
        customers = new HashMap<>();
        customers = addTestData();
    }

    private HashMap<String, Customer> addTestData() {
        HashMap<String, Customer> customers = new HashMap<>();

        Customer bhagy = new Customer();
        bhagy.setPassword("MyPa55w0rd");
        bhagy.addAccount(new Account("Main", 1000.0, 1234));
        bhagy.updateDetail("Baby Hagy", new GregorianCalendar(1982, Calendar.DECEMBER, 20).getTime(), "bhagy@bath.ac.uk", "Bath, London");
        customers.put("Bhagy", bhagy);

        Customer christina = new Customer();
        christina.setPassword("MyPa55w0rd");
        christina.addAccount(new Account("Savings", 1500.0, 2580));
        christina.updateDetail("Christina Aguilera", new GregorianCalendar(1985, Calendar.JANUARY, 11).getTime(), "christina.aguilera@celebrity.com", "Houston, USA");
        customers.put("Christina", christina);

        Customer john = new Customer();
        john.setPassword("MyPa55w0rd");
        john.addAccount(new Account("Checking", 250.0, 9876));
        john.addAccount(new Account("Savings", 50.0, 3490));
        john.updateDetail("John Doe", new Date(), "john.doe@newbank.com", "Lagos, Nigeria");
        customers.put("John", john);

        return customers;
    }

    public HashMap<String, Customer> getCustomers() {
        return customers;
    }

    public Customer getCustomerByName(String customer) {
        return customers.get(customer);
    }

    public void addNewCustomer(String customerName, Customer customer) {
        customers.put(customerName, customer);
    }
}
