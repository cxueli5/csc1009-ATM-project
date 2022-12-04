package Actions;

// files imported from other packages
import Hardware.Keypad;
import Hardware.ScreenDisplay;
import Admin.BankDatabase;
import Admin.TransactionInfo;

// import java packages
import java.time.LocalDate;

/*****************************************************************
* performs transfer of amount from one account to another account
*****************************************************************/
public class Transfer extends Transaction {
    // intialised variables to be used
    private float amount;
    private int toAcc;
    private static final int CANCELLED = 0; //constant for cancelled option

    // variables from other objects in other packages to be used here
    private Keypad keypad;
    private BankDatabase bankDatabase = getBankDatabase();
    private ScreenDisplay screenDisplay = getScreenDisplay();

    //  input-specifier constants (large numbers so we don't clash with other already-declared constants)
    private static final String DOLLAR_CENTS_FORMAT = "dollarcents";
    private static final String INTEGER_FORMAT = "integer";

     // transfer constructor
    public Transfer(int userAccountNumber, ScreenDisplay atmScreenDisplay, BankDatabase atmBankDatabase, Keypad atmKeypad ){
        super(userAccountNumber, atmScreenDisplay, atmBankDatabase);
        this.keypad = atmKeypad;
    }

    /*****************************************************************
    * implements abstract execute() from Transaction superclass
    *****************************************************************/
    public void execute() throws Exception{
        // get user's choice of account number which they want to transfer money to
        this.toAcc = promptForToAcct();

        // if user has input a valid account number, proceed to prompt amount they want to transfer
        if (this.toAcc!=0 && this.toAcc!=1) {
            this.amount = promptForTranferAmt();
        }

        // if user has input an invalid account number, show error message
        else{
            screenDisplay.displayMessageLine("\nInvalid account. Cancelling Transaction...");
        }
        // if user has input the account number they want to transfer to and the amount they want to transfer
        if (this.amount!=0 && this.toAcc!=0 && this.toAcc!=1) {
            // deduct amount user wants to transfer from their account
            bankDatabase.debit(getAccountNumber(), amount);
            // add amount user wanted to transfer to the other account number which user has indicated earlier
            bankDatabase.credit(toAcc, amount);

            //create transaction log for current account,
            LocalDate dateOfTransaction = LocalDate.now();
            TransactionInfo currentTransaction = new TransactionInfo(dateOfTransaction, getAccountNumber(), "Transfer", amount, "",
                                                bankDatabase.getTotalBalance(getAccountNumber()), bankDatabase.getAvailableBalance(getAccountNumber()));
            // update transaction database
            bankDatabase.createTransactionRecord(currentTransaction);

            //create transaction log for the recipient account
            TransactionInfo currentToTransaction = new TransactionInfo(dateOfTransaction, toAcc, "Transfer", amount, "", 
                                                    bankDatabase.getAvailableBalance(toAcc), bankDatabase.getTotalBalance(toAcc));
            // update transaction database
            bankDatabase.createTransactionRecord(currentToTransaction);
            screenDisplay.displayMessage("Transfer successful. Remaining (available) balance: ");
            screenDisplay.displayDollarAmount(bankDatabase.getAvailableBalance(getAccountNumber()));
            screenDisplay.displayMessageLine("");
        }
        // otherwise, go back to main menu
        else{
            screenDisplay.displayMessageLine("Cancelling transasction...");
        }
    }

    /***************************************************
    * prompts user the account they want to transfer to
    ****************************************************/
    private int promptForToAcct() {
        // display prompt message on ATM screen
        ScreenDisplay screenDisplay = getScreenDisplay();
        screenDisplay.displayMessage("\nPlease enter account number to transfer to (press 0 to cancel): ");
        // get user's input from keypad
        int input = keypad.getNumericInput(INTEGER_FORMAT, 6); //accepts only 6 digit inputs, or 0 to cancel
        // validate if account number that user has keyed in is a valid account number
        if (input != 0 && bankDatabase.validateAccount(input)){
            // valid, return account number user has input
            return input;
        }
        else{
            // otherwise, perform cancel transaction
            screenDisplay.displayMessageLine("Invalid account number. \nCancelling transaction...");
            return CANCELLED;
        }
    }

    /***********************************************
    * prompts user amount they want to transfer
    ***********************************************/
    private float promptForTranferAmt() throws Exception {
        ScreenDisplay screenDisplay = getScreenDisplay();
        screenDisplay.displayMessage("\nCurrent available balance is ");
        screenDisplay.displayDollarAmount(bankDatabase.getAvailableBalance(getAccountNumber()));
        screenDisplay.displayMessage("\nPlease enter an amount to transfer in DOLLARS (press 0 to cancel): ");
        // get user's input on keypad
        float input = keypad.getNumericInput(DOLLAR_CENTS_FORMAT);
        // if user choose 0 to cancel. 0.0 or 0.00 also equal to CANCELLED
        if (input == CANCELLED) {
            // let user know transaction is cancelled
            screenDisplay.displayMessageLine("\nTransaction ended.");
            return CANCELLED;
        }
        // if user puts an invalid choice
        else if (input<CANCELLED){ //Should not come here unless programming error.
            // cancel user's transaction
            screenDisplay.displayMessageLine("\nInvalid amount. Cancelling Transaction...");
            throw new Exception("\nError at promptForTransferAmt(), accepting negative values.");
        }
        // if user has input an amount more than their account's cash balance
        else if(input>bankDatabase.getAvailableBalance(getAccountNumber())){
            // print message to inform user, amount input has exceeded their account's balance
            screenDisplay.displayMessageLine("\nAmount entered is more than available balance. Cancelling Transaction...");
            // cancel user's transaction
            return  CANCELLED;
        }
        // otherwise, proceed with transfer transaction
        else {
            return (float)input;
        }
    }

}
