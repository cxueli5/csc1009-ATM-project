package Admin;

// files imported from other packages
import Hardware.CashDispenser;
import Hardware.Keypad;
import Hardware.ScreenDisplay;

/****************************
* bank administrator's account
*****************************/
public class AdminAccount {
    // variables from other objects in other packages to be used here
    private Keypad keypad;
    private CashDispenser cashDispenser;
    private ScreenDisplay screenDisplay;
    private BankDatabase bankDatabase;
    //  input-specifier constants
    private static final String INTEGER_FORMAT = "integer";
    private static final String NAME_FORMAT = "name";
    private static final String CANCELLED_STR = "0";
    private static final int CANCELLED = 0;

    /****************************
    * admin account constructor
    *****************************/
    public AdminAccount(ScreenDisplay atmScreenDisplay, BankDatabase atmBankDatabase,Keypad atmKeypad, CashDispenser atmCashDispenser){
        this.screenDisplay=atmScreenDisplay;
        this.bankDatabase=atmBankDatabase;
        this.keypad=atmKeypad;
        this.cashDispenser=atmCashDispenser;
    }

    /*****************************************************
    * bank's admin can perform creation of user account
    ******************************************************/
    public void createAccount() throws Exception {
        // intialised variables
        String name;
        int pin;
        int accountID;

        // prompt user to input account name
        screenDisplay.displayMessageLine("Enter name (only letters allowed, enter 0 to cancel): ");
        // get user's input from keypad
        name=keypad.getAlphanumericInput(NAME_FORMAT); //keypad takes care of formatting, so we can just accept the input here
        if(!name.equals(CANCELLED_STR)){
            do {
                // prompt user to input account number
                screenDisplay.displayMessageLine("Please enter a six-digit accountID (enter 0 to cancel): ");
                // gets user's input from keypad
                accountID = keypad.getNumericInput(INTEGER_FORMAT, 6);
                if(accountID == CANCELLED){
                    break;
                }
                if (bankDatabase.validateAccount(accountID)){
                    screenDisplay.displayMessageLine("Account ID already exists. Please enter another valid accountID.");
                }
                else if(String.valueOf(accountID).length()!=6){
                    screenDisplay.displayMessageLine("Account ID entered does not have 6 digits. Please enter a valid account ID.");
                }
            }while(bankDatabase.validateAccount(accountID) || String.valueOf(accountID).length()!=6); //accountID must be unique, and 6 digits in length
                
            if(accountID != CANCELLED){
                // prompt user to set a pin number
                do {
                    screenDisplay.displayMessageLine("Please enter a six-digit pin (enter 0 to cancel): ");
                    // gets user's input from keypad
                    pin = keypad.getNumericInput(INTEGER_FORMAT, 6);
                    if(pin == CANCELLED){
                        break;
                    }
                    if(String.valueOf(pin).length()!=6 && pin != CANCELLED){
                        screenDisplay.displayMessageLine("Pin entered does not have 6 digits. Please enter a valid pin.");
                    }
                }while(String.valueOf(pin).length()!=6 );//&& pin != CANCELLED
                
                if (pin!= 0){
                    // add new account created into bank's database
                    bankDatabase.createAccountsRecord(new Account(name,accountID,pin,0,0));
                    // successfull created account
                    screenDisplay.displayMessageLine("You have successfully created an account with the ID of " + accountID);
                }
                else{
                    screenDisplay.displayMessageLine("Account creation cancelled");
                }
            }
            else{
                screenDisplay.displayMessageLine("Account creation cancelled");
            }
        }
        else{
            screenDisplay.displayMessageLine("Account creation cancelled");
        }
    }

    /*****************************************************
    *admin can check if ATM has enough cash to dispense
    *****************************************************/
    public void checkRemainingBills(){
        screenDisplay.displayMessageLine("The amount of $10 bills remaining are: " + cashDispenser.getBillsRemaining());
    }
}
