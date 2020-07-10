package newbank.server;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class CustomerIDTest extends TestCase {
    CustomerID customerID;

    @Test
    public void testGetKey() {
        customerID = new CustomerID("123XYZ");
        assertEquals("123XYZ", customerID.getKey());
    }

    @Test
    public void testGetKeyNull() {
        customerID = new CustomerID(null);
        assertNull(customerID.getKey());
    }
}
