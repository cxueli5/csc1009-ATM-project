package Maintenance;

// import java packages
import static org.junit.Assert.assertEquals;
import org.junit.Test;

// files imported from other packages
import Hardware.CashDispenser;

/*****************************************************
* unit testing for CashDispenser.java
*****************************************************/
public class CashDispenserTest {

    /*****************************************************
    * test case for sufficientCashAvailable()
    *****************************************************/
    @Test
    public void testSufficientCashAvailable() {
        var test = new CashDispenser();
        int amount = 100000;
        boolean checkCash = true;
        boolean testFunction = test.sufficientCashAvailable(amount);
        assertEquals(checkCash, testFunction);
    }

    /*****************************************************
    * test case for getBillsRemaining()
    *****************************************************/
    @Test
    public void testGetBillsRemainaing(){
        var test = new CashDispenser();
        int testFunc = test.getBillsRemaining();
        assertEquals(100000, testFunc);
    }
}
