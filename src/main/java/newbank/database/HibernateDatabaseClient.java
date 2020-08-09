package newbank.database;

import newbank.database.exception.NewBankDatabase;
import newbank.server.Account;
import newbank.server.Customer;
import newbank.server.CustomerID;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.*;

public class HibernateDatabaseClient implements DatabaseClient {
    private final HibernateUtility hibernateUtility;

    public HibernateDatabaseClient(HibernateUtility hibernateUtility) {
        this.hibernateUtility = hibernateUtility;
    }

    @Override
    public Collection<Customer> getCustomers() {
        try (Session session = hibernateUtility.getSessionFactory().openSession()) {
            return session.createQuery("select c from Customer c", Customer.class)
                    .getResultList();
        } catch (Exception e) {
            throw new NewBankDatabase("Failed to get customers", e);
        }
    }

    @Override
    public Customer getCustomerById(String customer) {
        try (Session session = hibernateUtility.getSessionFactory().openSession()) {
            Query<Customer> customerQuery = session.createQuery("select c from Customer c where c.customerID = :customer_id", Customer.class)
                    .setParameter("customer_id", customer);
            return customerQuery
                    .getSingleResult();
        }catch (NoResultException e){
            System.err.printf("CustomerID %s was not found%n", customer);
            return null;
        } catch (Exception e) {
            throw new NewBankDatabase("Failed to get customer by id", e);
        }
    }

    @Override
    public void addNewCustomer(Customer customer) {
        Transaction transaction = null;
        try (Session session = hibernateUtility.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                safeRollback(transaction);
            }
            throw new NewBankDatabase("Failed to get customers", e);
        }
    }

    private void safeRollback(Transaction transaction) {
        try {
            transaction.rollback();
        } catch (Exception e) {
            System.err.println("Failed to rollback transaction");
        }
    }

    @Override
    public boolean hasCustomer(String key) {
        return getCustomerById(key) != null;
    }

    @Override
    public void addAccount(CustomerID customerID, Account account) {
        Transaction transaction = null;
        try (Session session = hibernateUtility.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Customer customerById = getCustomerById(customerID);
            customerById.addAccount(account);
            session.update(customerById);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                safeRollback(transaction);
            }
            throw new NewBankDatabase("Failed to add new account", e);
        }
    }

    @Override
    public List<Account> getAccounts(String customer) {
        return getCustomerById(customer).getAccounts();
    }

    @Override
    public String getAccountsAsString(String customer) {
        return getCustomerById(customer).accountsToString();
    }

    @Override
    public void updateCustomer(Customer customer) {
        Transaction transaction = null;
        try (Session session = hibernateUtility.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                safeRollback(transaction);
            }
            throw new NewBankDatabase("Failed to add new account", e);
        }
    }

    public void addTestData() {
        String password = "MyPa55w0rd";

        Customer bhagy = new Customer(new CustomerID("Bhagy"));
        bhagy.setPassword(password);
        bhagy.addAccount(new Account("Main", 1000.0, 1234));
        bhagy.updateDetail("Baby Hagy", new GregorianCalendar(1982, Calendar.DECEMBER, 20).getTime(), "bhagy@bath.ac.uk", "Bath, London");

        Customer christina = new Customer(new CustomerID("Christina"));
        christina.setPassword(password);
        christina.addAccount(new Account("Savings", 1500.0, 2580));
        christina.updateDetail("Christina Aguilera", new GregorianCalendar(1985, Calendar.JANUARY, 11).getTime(), "christina.aguilera@celebrity.com", "Houston, USA");

        Customer john = new Customer(new CustomerID("John"));
        john.setPassword(password);
        john.addAccount(new Account("Checking", 250.0, 9876));
        john.addAccount(new Account("Savings", 50.0, 3490));
        john.updateDetail("John Doe", new Date(), "john.doe@newbank.com", "Lagos, Nigeria");

        try {
            this.addNewCustomer(bhagy);
            this.addNewCustomer(christina);
            this.addNewCustomer(john);
        }catch (Exception e){
            //DO Nothing
        }
    }
}
