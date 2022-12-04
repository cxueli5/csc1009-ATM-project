package Actions;

// files imported from other packages
import Hardware.Keypad;
import Hardware.ScreenDisplay;
import Admin.BankDatabase;


// converts balance amount to a different currency
public class CurrencyConvertor extends BalanceCheck {
    // intialised variables to be used
    private float conversionRate;
    private static final float SG_USD_RATE = 0.74f;
    private static final float SG_KRW_RATE = 899.42f;
    private static final float SG_HKD_RATE = 5.77f;

    // variables for switch case (user choice of currency to view in)
    private static final int SGD_TO_USD = 1;
    private static final int SGD_TO_KRW = 2;
    private static final int SGD_TO_HKD = 3;
    private static final int CANCELLED = 0;

    // objects from other packages to be used here
    private Keypad keypad;
    private BankDatabase bankDatabase = getBankDatabase(); // to link BankDatabase object from ATM.java class
    private ScreenDisplay screenDisplay = getScreenDisplay(); // to link ScreenDisplay object from ATM.java class

    //  input-specifier constants
    private static final String INTEGER_FORMAT = "integer";
    
    // currency convertor constructor
    public CurrencyConvertor(int userAccountNumber, ScreenDisplay atmScreen, BankDatabase atmBankDatabase, Keypad atmKeypad) {
        super(userAccountNumber, atmScreen, atmBankDatabase, atmKeypad);
        keypad = atmKeypad;
    }
    
    /*************************************************************
    *convert balance amount from SGD to currency of user's choice
    *************************************************************/
    public void execute() throws Exception {    
        // display menu options
        int currencySelect = displayCurrencyMenu();

        switch (currencySelect) {

            // convert to US Dollars
            case SGD_TO_USD:
                conversionRate = SG_USD_RATE;
                printCurrency("USD");   // call method to print SGD and USD balance
                break;

            // convert to Korean Won
            case SGD_TO_KRW:
                conversionRate = SG_KRW_RATE;
                printCurrency("KRW");   // call method to print SGD and KRW balance
                break;

            //convert to Hong Kong Dollars
            case SGD_TO_HKD:
                conversionRate = SG_HKD_RATE;
                printCurrency("HKD");   // call method to print SGD and HKD balance
                break;

            // end currency conversion program
            case CANCELLED:
                screenDisplay.displayMessageLine("\nExiting Currency Convertor..");
                break;

            // error message to display if feature is faulty (should not come to this unless there is programming fault)
            default:
                screenDisplay.displayMessageLine("\nSorry, unable to convert currency. Please contact 6527 8910 for assitance.");
                throw new Exception("Error at execute(), accepts input that is not one of the options.");
        }
    }

    /*************************************************************
    *show user choices of currency they can view their balance in
    *************************************************************/
    private int displayCurrencyMenu() {
        int choice = 5;
        while(choice >3){ // keypad will not allow for -ve inputs, so check only for inputs larger than max option (3 in this case)
            screenDisplay.displayMessage("\nChoose an option: ");
            screenDisplay.displayMessage("\n1 - SGD to USD");
            screenDisplay.displayMessage("\n2 - SGD to KRW");
            screenDisplay.displayMessage("\n3 - SGD to HKD");
            screenDisplay.displayMessage("\n0 - Exit Currency Convertor");
            screenDisplay.displayMessage("\n");
            screenDisplay.displayMessage("\nEnter choice: ");
            // get user's input from keypad
            choice = keypad.getNumericInput(INTEGER_FORMAT, 1);
        }
        return choice;
    }

    /*************************************************************
    *display balance after converting to requested currency
    *************************************************************/
    private void printCurrency(String country){
        // total balance: cash + approved & unapproved cheques
        // display total balance in SGD to converted currency
        screenDisplay.displayMessage("\nTotal Balance:      SGD");
        screenDisplay.displayDollarAmount(bankDatabase.getTotalBalance(getAccountNumber()));
        screenDisplay.displayMessage("\t " + country);
        screenDisplay.displayDollarAmount(conversionRate * bankDatabase.getTotalBalance(getAccountNumber()));
        // available balance: cash & approved cheque amounts only
        // display available balance in SGD to converted currency
        screenDisplay.displayMessage("\nAvailable Balance:  SGD");
        screenDisplay.displayDollarAmount(bankDatabase.getAvailableBalance(getAccountNumber()));
        screenDisplay.displayMessage("\t " + country);
        screenDisplay.displayDollarAmount(conversionRate * bankDatabase.getAvailableBalance(getAccountNumber()));
        screenDisplay.displayMessageLine("");
    }
}
