package newbank.server;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class CustomerIDTest {
    CustomerID customerID;

    @Test
    public void testGetKey() {
        customerID = new CustomerID("123XYZ");
        Assert.assertEquals("123XYZ", customerID.getKey());
    }

    @Test
    public void testGetKeyNull() {
        customerID = new CustomerID(null);
        Assert.assertNull(customerID.getKey());
    }
}
