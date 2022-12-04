package Maintenance;

// import java packages
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

// files imported from other packages
import Admin.Account;

/*****************************************************
* unit testing for Account.java
*****************************************************/
public class AccountTest {
    
    /*****************************************************
    * test case for getAccountNumber()
    *****************************************************/
    @Test
    public void testGetAccountNumber() {
        var test3 = new Account("sarah", 394930, 495040, 1000.0f, 1000.0f);
        int testFunc = test3.getAccountNumber();
        assertEquals(394930, testFunc);
    }

    /*****************************************************
    * test case for getName()
    *****************************************************/
    @Test
    public void testGetName() {
        var test3 = new Account("sarah", 394930, 495040, 1000.0f, 1000.0f);
        String testFunc = test3.getName();
        assertEquals("sarah", testFunc);
    }

    /*****************************************************
    * test case for getAvailableBalance()
    *****************************************************/
    @Test
    public void testGetAvailableBalance() {
        var test3 = new Account("sarah", 394930, 495040, 1000.0f, 1000.0f);
        double testFunc = test3.getAvailableBalance();
        assertEquals(1000.0, testFunc, 1.00);
    }

    /*****************************************************
    * test case for getTotalBalance()
    *****************************************************/
    @Test
    public void testGetTotalBalance() {
        var test3 = new Account("sarah", 394930, 495040, 1000.0f, 1000.0f);
        double testFunc = test3.getTotalBalance();
        assertEquals(1000.0, testFunc, 1.00);
    }

    /*****************************************************
    * test case for changePin()
    *****************************************************/
    @Test
    public void testChangePin(){
        var test3 = new Account("sarah", 394930, 495040, 1000.0f, 1000.0f);
        int testNewPin = 1111;
        test3.changePin(testNewPin);
        int testFunc = test3.getPin();
        assertEquals(testNewPin, testFunc);
    }

    /*****************************************************
    * test case for validatePin()
    *****************************************************/
    @Test
    public void testValidatePin(){
        var test3 = new Account("sarah", 394930, 495040, 1000.0f, 1000.0f);
        int pin = 495040;
        boolean testFunc = test3.validatePin(pin);
        assertTrue(testFunc == true);
    }
    

}
