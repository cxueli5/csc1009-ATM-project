package Maintenance;

// import java packages
import static org.junit.Assert.assertEquals;
import org.junit.Test;

// files imported from other packages
import Admin.BankDatabase;

/*****************************************************
* unit testing for BankDatabase.java
*****************************************************/
public class BankDatabaseTest {
    
    /*****************************************************
    * test case for authenticateCheque()
    *****************************************************/
    // check if cheque number deposited exists in database as cheque numbers must be unique
    @Test
    public void testAuthenticateCheque() {
        String testCheqNo = "AA4123";
        boolean checkTest;
        if (testCheqNo == "AA4123") {
            checkTest = true; // cheque number exists in database
        } else {
            checkTest = false; // cheque number does not exists in database
        } 
        var test2 = new BankDatabase();
        boolean testFunc = test2.authenticateCheque(testCheqNo);
        assertEquals(checkTest, testFunc);

    }

    /*****************************************************
    * test case for recordCheque()
    *****************************************************/
    // test if can add cheque into cheque database
    @Test
    public void testRecordCheque() {
        String testAddCheque = "EE1234";
        var test2 = new BankDatabase();
        test2.recordCheque(testAddCheque); // add new cheque number into cheque database
        // check if cheque number exists in database
        boolean checkTest;
        if (testAddCheque == "EE1234") {
            checkTest = true; // cheque number exists in database
        } else {
            checkTest = false; // cheque number does not exists in database
        } 
        boolean testFunc = test2.authenticateCheque(testAddCheque);
        assertEquals(checkTest, testFunc);
    }

}
