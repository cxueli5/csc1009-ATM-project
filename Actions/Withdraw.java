package Actions;

// files imported from other packages
import Hardware.CashDispenser;
import Hardware.Keypad;
import Hardware.ScreenDisplay;
import Admin.BankDatabase;
import Admin.TransactionInfo;

// import java packages
import java.time.LocalDate;

// withdraw amount user wants from their account
public class Withdraw extends Transaction {
    // intialised variables to be used
    private int amount;
    private static final int CANCELLED = 0;

    // variables from other objects in other packages to be used here
    private Keypad keypad;
    private CashDispenser cashDispenser;

    //  input-specifier constants (large numbers so we don't clash with other already-declared constants)
    private static final String INTEGER_FORMAT = "integer";
    // withdraw constructor
    public Withdraw(int userAccountNumber, ScreenDisplay atmScreen, BankDatabase atmBankDatabase, Keypad atmKeypad, CashDispenser atmCashDispenser){
        super(userAccountNumber, atmScreen, atmBankDatabase);
        keypad = atmKeypad;
        cashDispenser = atmCashDispenser;
    }

    // execute withdraw transaction
    public void execute() throws Exception { 
        // intialise cash dispense as false (as no cash has been dispensed yet)
        boolean cashDispensed = false;
        // get user's cash balance
        double availableBalance; //amount available for withdrawal

        BankDatabase bankDatabase = getBankDatabase();
        ScreenDisplay screenDisplay = getScreenDisplay();

        do{
            // display choices of amount user can withdraw
            amount = displayMenuOfAmounts();
            // if user choose an amount and not cancel transaction
            if(amount != CANCELLED){
                // get user's account cash balance
                availableBalance = bankDatabase.getAvailableBalance(getAccountNumber());
                // if amount user has chosen is within their account's available cash balance
                if (amount <= availableBalance){
                    // check if ATM has enough cash to dispense
                    if(cashDispenser.sufficientCashAvailable(amount)){
                        // if ATM has enough, deduct amount user has chosen from their account
                        bankDatabase.debit(getAccountNumber(), amount);
                        // proceed to deposit cash
                        cashDispenser.dispenseCash(amount);
                        // change cash dispense from false to true
                        cashDispensed = true;
                        // show user their account's cash balance
                        availableBalance = bankDatabase.getAvailableBalance(getAccountNumber());
                        screenDisplay.displayMessageLine("\nRemaining balance: " + availableBalance);                        
                        // inform user to take the cash dispensed from ATM
                        screenDisplay.displayMessageLine("\nYour cash has been dispensed. Please take your cash now.");
                        // store transaction details
                        LocalDate dateOfTransaction = LocalDate.now();
                        double totalBalance = bankDatabase.getTotalBalance(getAccountNumber());
                        TransactionInfo currentTransaction = new TransactionInfo(dateOfTransaction, getAccountNumber(), "Withdraw", amount, "", totalBalance, availableBalance);
                        bankDatabase.createTransactionRecord(currentTransaction);
                    }
                    else{
                        // ATM does not have enough cash to dispense user's choice of amount
                        // show error message
                        screenDisplay.displayMessageLine("\nInsufficient cash available in the ATM. \nPlease choose a smaller amount.");
                    }
                }
                else{
                    // user's account does not have enough cash balance to withdraw amount they have chosen
                    // show error message
                    screenDisplay.displayMessageLine("\nInsufficient funds in your account.");
                    // show user's current cash balance in account. Prompt user to choose a smaller amount.
                    screenDisplay.displayMessageLine("\nPlease choose a smaller amount. Current balance: $" + availableBalance);
                }
            }
            else{
                // user cancels transaction, show message
                screenDisplay.displayMessageLine("\nCancelling transaction...");
                return; //return to main menu since user cancelled
            }
        }
        while(!cashDispensed);
    }

    // show amount choices that user can choose to withdraw
    private int displayMenuOfAmounts() throws Exception{ 
        int userChoice = -1;
        int input = 6;
        ScreenDisplay screenDisplay = getScreenDisplay();
        // integer array to store the choices of amount which user can choose to withdraw
        int[] amounts = {0, 20, 50, 100, 500, 1000};

        // when user have yet input their choice, show them what choices are available
        while(input>5){
            screenDisplay.displayMessageLine("\nWithdrawal Amounts: ");
            screenDisplay.displayMessageLine("1 - $20");
            screenDisplay.displayMessageLine("2 - $50");
            screenDisplay.displayMessageLine("3 - $100");
            screenDisplay.displayMessageLine("4 - $500");
            screenDisplay.displayMessageLine("5 - $1000");
            screenDisplay.displayMessageLine("0 - Cancel Transaction");
            screenDisplay.displayMessage("\nChoose a withdrawal amount: ");

            // get user's input from keypad
            input = keypad.getNumericInput(INTEGER_FORMAT, 1);
            //input should always be >0. if >5, prompt again
        }

        // based on user's input in keypad, get the amount they have chosen
        switch (input){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                userChoice = amounts[input];
                break;
            // if user has chosen to cancel transaction, perform cancel transaction
            case CANCELLED:
                userChoice = CANCELLED;
                break;
            
            // should not come to this unless there is a programming issue. throw exception.
            default:
                throw new Exception("Error at displayMenuOfAmounts(), accepting values other than the available options.");
        }
        
        // if user has chosen an amount, return the amount user has chosen
        return userChoice;
    }
}
