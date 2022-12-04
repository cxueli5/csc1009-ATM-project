package Actions;
// files imported from other packages
import Hardware.DepositSlot;
import Hardware.Keypad;
import Hardware.ScreenDisplay;
import Admin.BankDatabase;
import Admin.TransactionInfo;

// import java packages
import java.time.LocalDate;

/****************************************
*       deposits cash/cheque
*****************************************/
public class Deposit extends Transaction {

//  initialised variables to be used
    private LocalDate dateOfTransaction;
    private float amount;
    private int depositType;
    private Keypad keypad;
    private DepositSlot depositSlot;

    private static final int CANCELLED = 0; 
    private static final int CASH = 1;
    private static final int CHEQUE = 2;

//  input-specifier constants
    private static final String CHEQUE_FORMAT = "cheque";
    private static final String DOLLAR_CENTS_FORMAT = "dollarcents";
    private static final String INTEGER_FORMAT = "integer";
    
    /****************************************
    *       deposit constructor
    *****************************************/
    public Deposit(int userAccountNumber, ScreenDisplay atmScreenDisplay, BankDatabase atmBankDatabase, Keypad atmKeypad, DepositSlot atmDepositSlot) {
        super(userAccountNumber, atmScreenDisplay, atmBankDatabase);
        keypad = atmKeypad;
        depositSlot = atmDepositSlot;
    }

    /****************************************************************
    *implement abstract execute() method from Transaction superclass
    *****************************************************************/
    public void execute() throws Exception{
        BankDatabase bankDatabase = getBankDatabase();
        ScreenDisplay screenDisplay = getScreenDisplay();

        depositType = promptForDepositType();
        amount = promptForDepositAmount(depositType);

        // if user did not choose to cancel operation
        if (amount != CANCELLED) { // no need to check for amount <0, because keypad only accepts +ve inputs
            // user choose to deposit in cash
            if(depositType == CASH){
                screenDisplay.displayMessage("\nPlease insert cash in deposit slot containing amount of ");
                screenDisplay.displayDollarAmount(amount);

                // checks if user has deposited the cash and if ATN received the cash deposited
                boolean cashReceived = depositSlot.isReceived();

                // if received, show user amount deposited and total balance
                // otherwise, inform user failed to receive cash deposited
                if (cashReceived) {
                    bankDatabase.credit(getAccountNumber(), amount);
                    screenDisplay.displayMessageLine("\nCash received.");

                    screenDisplay.displayMessage("\nTotal balance: ");
                    screenDisplay.displayDollarAmount(bankDatabase.getTotalBalance(getAccountNumber()));
                    screenDisplay.displayMessage("\nAvailable balance: ");
                    screenDisplay.displayDollarAmount(bankDatabase.getAvailableBalance(getAccountNumber()));
                    screenDisplay.displayMessage("\n");
                    
                    // store transaction details
                    dateOfTransaction = LocalDate.now();
                    TransactionInfo currentTransaction = new TransactionInfo(dateOfTransaction, getAccountNumber(), "cashDeposit", amount, "", bankDatabase.getTotalBalance(getAccountNumber()), bankDatabase.getAvailableBalance(getAccountNumber()));
                    bankDatabase.createTransactionRecord(currentTransaction);
                
                } else {
                    screenDisplay.displayMessageLine("\nNo cash received. Cancelling transaction.");
                }
            }

            // if user choose to deposite cheque
            if(depositType == CHEQUE){
                // get user's input on cheque number
                String chequeNumber = "";
                screenDisplay.displayMessage("\nPlease enter cheque number (enter 0 to cancel): ");
                // chequeNumber input must be 6 characters long, specified in CHEQUE_FORMAT
                // Also accept "0", user cancelled
                chequeNumber = keypad.getAlphanumericInput(CHEQUE_FORMAT); //no need to loop here, loops in keypad until valid input received

                // authenticate if cheque number input is valid
                boolean validCheque = bankDatabase.authenticateCheque(chequeNumber);
                // if cheque is valid, allow user to deposit cheque value amount
                if(validCheque){
                    screenDisplay.displayMessage("\nPlease insert cheque in deposit slot containing amount of ");
                    screenDisplay.displayDollarAmount(amount);

                    // check if cheque value has been received by ATM
                    boolean chequeReceived = depositSlot.isReceived();

                    // if ATM received cheque successfully
                    if (chequeReceived) {
                        // update cheque records in database for the user's account
                        bankDatabase.recordCheque(chequeNumber); //add cheque number to bankDatabase records
                        bankDatabase.creditCheque(getAccountNumber(), amount); //credit amount only to total balance, add cheque to list of cheques to be approved 
                        
                        screenDisplay.displayMessageLine("\nCheque received.");

                        // show user account balance
                        
                        // total balance : cash + approved & unapproved cheque deposits
                        screenDisplay.displayMessage("\nTotal balance: ");
                        screenDisplay.displayDollarAmount(bankDatabase.getTotalBalance(getAccountNumber())); // retrieve total balance information from bank database
                        
                        // available balance : cash + approved cheque deposits
                        screenDisplay.displayMessage("\nAvailable balance: ");
                        screenDisplay.displayDollarAmount(bankDatabase.getAvailableBalance(getAccountNumber())); // retrieve available balance information from bank database
                        
                        screenDisplay.displayMessageLine("\nThe amount will be credited to your account after processing the cheque.");
                        // store transaction details
                        dateOfTransaction = LocalDate.now();
                        TransactionInfo currentTransaction = new TransactionInfo(dateOfTransaction, getAccountNumber(), "chequeDeposit", amount, chequeNumber, 
                                                                                 bankDatabase.getTotalBalance(getAccountNumber()), bankDatabase.getAvailableBalance(getAccountNumber()));                        
                        bankDatabase.createTransactionRecord(currentTransaction);
                    } else {
                        // unsuccessful deposit of cheque
                        screenDisplay.displayMessageLine("\nCheque not received. Cancelling transaction.");
                    }
                }
                else{
                    // invalid cheque
                    screenDisplay.displayMessageLine("\nInvalid cheque number. Cancelling transaction.");
                }
            }
        }
        // user choose to cancel transaction
        else {
            screenDisplay.displayMessageLine("\nCancelling Transaction...");
        }
    }

    /************************************************
    /* prompt user for amount they want to deposit
    *************************************************/
    private float promptForDepositAmount(int depositType){
        ScreenDisplay screenDisplay = getScreenDisplay();
        float input = 0f;
        if(depositType == CASH)
        {
            boolean validInput = false;
            while(validInput == false){
                screenDisplay.displayMessage("\nPlease enter an amount to deposit, in denominations of $10 (enter 0 to cancel): ");
                // getNumericInput(INTEGER_FORMAT) accepts and returns only positive whole numbers
                input = keypad.getNumericInput(INTEGER_FORMAT); 
                if(input%10 == 0){      // further requirement: only accept denominations of $10
                    validInput = true;
                }
                if (input == CANCELLED) {   // if user enters 0
                    validInput = true;
                    return CANCELLED;
                }
            }
            return input;
        }
        else
        {
            screenDisplay.displayMessage("\nPlease enter the cheque's value (enter 0 to cancel): ");
            // expecting dollar and cents input, $x, $x.x, $x.xx accepted
            // only need to check for 0 values, negative/character inputs are prevented by keypad.getNumericInput
            input = keypad.getNumericInput(DOLLAR_CENTS_FORMAT);
            if (input == CANCELLED) {   //if user enters 0
                return CANCELLED;
            }
            return input;
        }
    }
    
    /*************************************************
    * prompt for type of deposit: cash or cheque
    *************************************************/
    private int promptForDepositType() throws Exception{
        ScreenDisplay screenDisplay = getScreenDisplay();
        int input = 3;
        while(input > 2){ // should never be -ve, handled by keypad. else, will throw exception below
            screenDisplay.displayMessage("\nSelect deposit type: ");
            screenDisplay.displayMessage("\n1 - Cash ");
            screenDisplay.displayMessageLine("\n2 - Cheque");
            // gets user input of choice
            input = keypad.getNumericInput(INTEGER_FORMAT, 1); //get single digit input
        }
        switch(input){
            case CASH:   // if user choose to deposit cash, perform cash deposit transaction
            case CHEQUE: // if user choose to deposit cheque, perform cheque deposit transaction
            case CANCELLED:    // if user choose to cancel transaction, perform cancel transaction
                return input; // same as return CASH/CHEQUE/CANCELLED
            default:
                throw new Exception("Error at promptForDepositType(), user able to input options other than listed options");

        }
    }
}
