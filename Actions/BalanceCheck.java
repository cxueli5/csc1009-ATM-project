package Actions;

// files imported from other packages
import Hardware.Keypad;
import Hardware.ScreenDisplay;
import Admin.BankDatabase;

/*******************************************
*       checks user's account balance
********************************************/
public class BalanceCheck extends Transaction {
    // variables from other objects in other packages to be used here
    private BankDatabase bankDatabase = getBankDatabase(); //retrieve bankDatabase used by Transaction
    private ScreenDisplay screenDisplay = getScreenDisplay();
    private Keypad keypad;
    
    // variables for switch case (user choice to view balance in another currency)
    private static final int YES = 1;
    private static final int CANCELLED = 0;

    //  input-specifier constant for keypad
    private static final String INTEGER_FORMAT = "integer";

    // balance check constructor
    public BalanceCheck(int userAccountNumber, ScreenDisplay atmScreen, BankDatabase atmBankDatabase, Keypad atmKeypad){
        super(userAccountNumber, atmScreen, atmBankDatabase); //calls Transaction constructor
        keypad = atmKeypad;
    }

    /*******************************************
    * implements abstract execute() in Transaction
    ********************************************/
    public void execute() throws Exception{  

        // display account balances in SGD
        screenDisplay.displayMessageLine("\nBalance Information:");

        // total balance: cash + approved & unapproved cheques
        screenDisplay.displayMessage("- Total Balance: ");
        screenDisplay.displayDollarAmount(bankDatabase.getTotalBalance(getAccountNumber()));
        
        // available balance: cash & approved cheque amounts only
        screenDisplay.displayMessage("\n- Available Balance: ");
        screenDisplay.displayDollarAmount(bankDatabase.getAvailableBalance(getAccountNumber()));

        screenDisplay.displayMessageLine("");

        // allows user to have a choice to view balance in another currency
        convertBalanceCurrency();
    }

    
    /*************************************************
    * prompt user to view balance in another currency
    **************************************************/
    private int displayPromptConvertCurrency(){
        int choice = 5;
        while(choice >1){ //keypad won't allow -ve inputs, so only check if input is larger than max choice (1 in this case)
            screenDisplay.displayMessage("\nView balance in different currency?: ");
            screenDisplay.displayMessage("\n1 - YES");
            screenDisplay.displayMessage("\n0 - NO");
            screenDisplay.displayMessage("\n");
            screenDisplay.displayMessage("\nChoose an option: ");
            // get user's input from keypad
            choice = keypad.getNumericInput(INTEGER_FORMAT, 1);
        }
        return choice; // will always be 1 of the options listed
    }
    
    /**********************************************************
    * ask user if they want to view balance in another currency
    ***********************************************************/
    private void convertBalanceCurrency() throws Exception{
        // display menu options
        int choiceSelect = displayPromptConvertCurrency();

        // execute accordingly
        if(choiceSelect == YES){ // user wants to view balance in another currency
            CurrencyConvertor current = new CurrencyConvertor(getAccountNumber(), screenDisplay, bankDatabase, keypad);
                current.execute();
        }
        // user does not want to view balance in another currency
        else if(choiceSelect == CANCELLED)
        {   
            screenDisplay.displayMessage("\nExiting Currency Convertor..");
        }
        else{ // should not come here unless have programming errors
            throw new Exception("\nError at displayPromptConverCurrency(), accepting input outside given options");
        }
    }
}
