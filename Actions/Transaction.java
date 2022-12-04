package Actions;

// files imported from other packages
import Hardware.ScreenDisplay;
import Admin.BankDatabase;

/***********************************************************************************************
* Parent class for all transaction classes in Actions, such as Withdraw, Deposit, Transfer etc.
***********************************************************************************************/
public abstract class Transaction {
    // intialised variables to be used
    private int accountNumber;
    
    // objects from other packages to be used here
    private ScreenDisplay screenDisplay;
    private BankDatabase bankDatabase;

    /*************************
    * Transaction constructor
    **************************/
    public Transaction(int userAccountNumber, ScreenDisplay atmScreen, BankDatabase atmBankDatabase){
        accountNumber = userAccountNumber;
        screenDisplay = atmScreen;
        bankDatabase = atmBankDatabase;
    }


    /****************************************************************
    *Returns user account number which was passed in from ATM.java
    *****************************************************************/
    public int getAccountNumber(){
        return accountNumber;
    }

    /******************************************
    *Returns ScreenDisplay object from ATM.java
    *******************************************/
    public ScreenDisplay getScreenDisplay(){
        return screenDisplay;
    }

    /******************************************
    *Returns BankDatabase object from ATM.java
    *******************************************/
    public BankDatabase getBankDatabase(){
        return bankDatabase;
    }

    /******************************************************
    *Abstract function to be implemented by its subclasses
    *******************************************************/
    public abstract void execute() throws Exception;
}
