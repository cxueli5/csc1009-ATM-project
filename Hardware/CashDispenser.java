package Hardware;

// ATM dispense cash
public class CashDispenser {
    // inital amount of cash in ATM
    private static final int INITIAL_BILL_COUNT = 100000; //Start with $1 million in ATM, $10 notes for simplicity
    // variable to update amount of cash remain inside ATM
    private int billsRemaining;

    // cash dispenser constructor
    public CashDispenser(){
        billsRemaining = INITIAL_BILL_COUNT;
    }

    // dispense cash from ATM
    public void dispenseCash(int amount){
        // get cash amount user wants to withdraw
        int billsRequired = amount/10;
        // update cash balance in ATM
        billsRemaining -= billsRequired;
    }

    // get amount of cash left in ATM
    public int getBillsRemaining(){
        return billsRemaining;
    }

    // check if there is enough cash in ATM to dispense
    public boolean sufficientCashAvailable(int amount){
        // get cash amount user wants to withdraw
        int billsRequired = amount/10;
        // if ATM has enough cash to dispense based on amount user wants to withdraw
        if (billsRemaining>=billsRequired){
            return true;
        }
        else{
            return false;
        }
    }
}
