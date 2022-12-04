package Actions;

// files imported from other packages
import Admin.BankDatabase;
import Hardware.Keypad;
import Hardware.ScreenDisplay;

/*****************************************************
* allows users to change their account's pin number
*****************************************************/
public class ChangePassword extends Transaction{
    // variables from other objects in other packages to be used here
    private Keypad keypad;
    private BankDatabase bankDatabase = getBankDatabase();
    private ScreenDisplay screenDisplay = getScreenDisplay();

    // intialised variables to be used
    private int newPin;
    private static final int CANCELLED = 0; //constant for cancelled option
    
    //  input-specifier constants (large numbers so we don't clash with other already-declared constants)
    private static final String INTEGER_FORMAT = "integer";

    // change password constructor
    public ChangePassword(int userAccountNumber, ScreenDisplay atmScreenDisplay, BankDatabase atmBankDatabase, Keypad atmKeypad ){
        super(userAccountNumber, atmScreenDisplay, atmBankDatabase);
        this.keypad = atmKeypad;
    }

    /**********************************************************
    * overwrites the abstract execute() from Transaction class
    ***********************************************************/
    public void execute() throws Exception {
        // get user input for new pin number
        newPin=promptForPin();
        // update user's account new pin number in bank database
        bankDatabase.changePin(getAccountNumber(),newPin);
        // display success message
        if(newPin != CANCELLED){    
            screenDisplay.displayMessageLine("Successfully changed PIN.");
        }
        else{
            screenDisplay.displayMessageLine("Cancelling transaction...");
        }
    }

    /**********************************************************
    * prompts and returns user input for a new PIN
    ***********************************************************/
    private int promptForPin() throws Exception {
        int input1; // 1st input of the pin
        int input2; // 2nd input of the pin for validation

        // prompt user to input their preferred new pin number
        screenDisplay.displayMessageLine("Please enter a six-digit PIN: ");
        input1 = keypad.getNumericInput(INTEGER_FORMAT, 6);
        
        if(input1 == CANCELLED){ // allow user to cancel
            return CANCELLED;
        }
        else if(String.valueOf(input1).length()!=6){    // this should not happen (hence used else if)
            throw new Exception("Error at promptForPin, digits accepted != 6");
        }
        
        // confirmation of new pin number
        screenDisplay.displayMessageLine("Please re-enter your new PIN for confirmation: ");
        input2 = keypad.getNumericInput(INTEGER_FORMAT, 6);
        
        if(input2 == CANCELLED){ // allow user to cancel
            return CANCELLED;
        }
        // validate if their initial input of their new pin number and re-entered pin numeber matches
        if (input1==input2){
            return input1;
        }
        // pin number does not match, ask user to enter their new pin number again
        else{
            screenDisplay.displayMessageLine("PINs do not match");
            return promptForPin();
        }

    }
}
