package newbank.database;

import newbank.server.Account;
import newbank.server.Customer;
import newbank.server.CustomerID;

import java.util.Collection;
import java.util.List;

public interface DatabaseClient {
    Collection<Customer> getCustomers();
    Customer getCustomerById(String customer);
    void addNewCustomer(Customer customer);
    boolean hasCustomer(String key);
    void addAccount(CustomerID customerID, Account account);
    List<Account> getAccounts(String customer);
    String getAccountsAsString(String customer);
    void updateCustomer(Customer customer);

    default Customer getCustomerById(CustomerID customer){
        return getCustomerById(customer.getKey());
    }
    default List<Account> getAccounts(CustomerID customer){
        return getAccounts(customer.getKey());
    }

    default String getAccountsAsString(CustomerID customer){
        return getAccountsAsString(customer.getKey());
    }
}
