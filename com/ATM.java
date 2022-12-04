package com;

// files imported from other packages
import Actions.*;
import Admin.AdminAccount;
import Admin.BankDatabase;
import Hardware.CashDispenser;
import Hardware.DepositSlot;
import Hardware.Keypad;
import Hardware.ScreenDisplay;



public class ATM {
    // initialization of variables
    private boolean userAuthenticated;
    private int currentAccountNumber;
    private ScreenDisplay screenDisplay;
    private Keypad keypad;
    private CashDispenser cashDispenser;
    private DepositSlot depositSlot;
    private BankDatabase bankDatabase;

    // corresponds to user input when shown the display screen on transactions to perform
    private static final int BALANCE_INQUIRY = 1;
    private static final int WITHDRAW = 2;
    private static final int DEPOSIT = 3;
    private static final int TRANSFER = 4;
    private static final int CHANGEPASSWORD=5;
    private static final int VIEW_TRANSACTION_HISTORY = 6;
    private static final int CANCELLED=0;

    // input-specifier constant
    private static final String INTEGER_FORMAT = "integer";
    private static final String DOLLAR_CENTS_FORMAT = "dollarcents"; //for exception example, in programming highlights
    
    public ATM(){
        userAuthenticated = false;
        currentAccountNumber = 0;

        // link the ATM object to hardware and database
        screenDisplay = new ScreenDisplay();
        keypad = new Keypad(screenDisplay); // needs to print msgs if there is invalid input
        cashDispenser = new CashDispenser();
        depositSlot = new DepositSlot();
        bankDatabase = new BankDatabase();
    }

    public void run() throws Exception {
        while(true){ // ATM Program constantly on to simulate actual ATM
            while(!userAuthenticated) {
                screenDisplay.displayMessageLine("\nWelcome to ABC Bank!");
                authenticateUser();
            }
            if (currentAccountNumber==111111) {
                adminTransactions(); // admin perform transaction
            }
            else {
                performTransactions(); // user performs their transactions
            }
            userAuthenticated = false; // user ended transactions, reset authentication before next user session
            currentAccountNumber = 0;
            screenDisplay.displayMessageLine("\nThank you for banking with us!");
        }
    }

    private void authenticateUser() { // method to authenticate user
        screenDisplay.displayMessage("\nPlease enter your account number: ");
        int accountNumber = keypad.getNumericInput(INTEGER_FORMAT, 6); // user input account number
        screenDisplay.displayMessage("\nEnter your PIN: ");
        int pin = keypad.getNumericInput(INTEGER_FORMAT, 6); // user input pin

        userAuthenticated = bankDatabase.authenticateUser(accountNumber, pin); // boolean to check if account number corresponds with pin in databse
        if (userAuthenticated) {
            currentAccountNumber = accountNumber; // if authenticated, let current account equals authenticated account
        } 
        else if (pin == CANCELLED){
            screenDisplay.displayMessageLine("Exiting sesson...");
            currentAccountNumber = 0;
        }
        else {
            screenDisplay.displayMessageLine("\nInvalid account number or PIN. Please try again.");
        }
    }
    private void adminTransactions() throws Exception { // method for admin's interface
        AdminAccount adminAccount=new AdminAccount(screenDisplay, bankDatabase,keypad,cashDispenser);
        boolean userExited = false;
        while(!userExited) { // while admin has not select exit
            int mainMenuSelection = displayAdminMenu();

            // switch case for admin choice of action
            switch (mainMenuSelection) {
                case 1:
                    adminAccount.createAccount(); // create new accounts
                    break;
                case 2:
                    adminAccount.checkRemainingBills(); // check remaining bills
                    break;
                case CANCELLED:
                    screenDisplay.displayMessageLine("\nLogging out...");
                    userExited = true; // admin exit
                    break;
                default:
                    throw new Exception("\nError at displayAdminMenu()/adminTransactions(), accepting inputs other than available options.");
            }
        }
    }

    private void performTransactions() throws Exception {// user perform transaction
        Transaction currentTransaction = null;

        boolean userExited = false;

        while(!userExited){     // while user has not exited
            int mainMenuSelection = displayMainMenu(); // print main menu and get choice of input

        // switch case for users choice of action
        switch(mainMenuSelection){
            case BALANCE_INQUIRY:
            case WITHDRAW:
            case TRANSFER:
            case DEPOSIT:
            case CHANGEPASSWORD:
            case VIEW_TRANSACTION_HISTORY:
                currentTransaction = createTransaction(mainMenuSelection); // create  action according to user input
                currentTransaction.execute();   // execute type of transaction
                break;
            case CANCELLED:
                // approve cheque deposits before logging out
                bankDatabase.approveCheques(currentAccountNumber);
                screenDisplay.displayMessageLine("\nLogging out...");
                userExited = true; // user exit
                break;
            default:
                throw new Exception("\nError at displayMainMenu()/performTransactions(), accepting inputs other than available options.");
            }
        }
    }
    private int displayMainMenu() { // display main menu
        // total 7 choices
        int input = 7;
        while(input >6){
            screenDisplay.displayMessageLine("\nMain menu:");
            screenDisplay.displayMessageLine("1 - Check Balance");
            screenDisplay.displayMessageLine("2 - Withdraw Cash");
            screenDisplay.displayMessageLine("3 - Deposit Funds");
            screenDisplay.displayMessageLine("4 - Transfer");
            screenDisplay.displayMessageLine("5 - Change password");
            screenDisplay.displayMessageLine("6 - View Transaction History");
            screenDisplay.displayMessageLine("0 - Exit");
            
            // if no bills in ATM
            if(cashDispenser.getBillsRemaining() == 0){
                screenDisplay.displayMessage("\nWithdraw function unavailable. Sorry for the inconvenience."); 
            }
            screenDisplay.displayMessage("\nSelect an option: ");
        // get user choice of input
        input = keypad.getNumericInput(INTEGER_FORMAT, 1);
        }
        return input;
    }
    private int displayAdminMenu() { // print admin menu
        int input = 3;
        while(input > 2){
            screenDisplay.displayMessageLine("\nAdmin menu:");
            screenDisplay.displayMessageLine("1 - Create Account");
            screenDisplay.displayMessageLine("2 - Check remaining bills");
            screenDisplay.displayMessageLine("0 - Exit");
            screenDisplay.displayMessage("\nSelect an option: ");
            input = keypad.getNumericInput(INTEGER_FORMAT, 1);  // takes in admin's choice of action
        }
        return input;
    }
    private Transaction createTransaction(int type){    // to create an action object
        Transaction temp = null;
        // switch case for creating action object
        switch(type){
            case BALANCE_INQUIRY:
                temp = new BalanceCheck(currentAccountNumber, screenDisplay, bankDatabase, keypad);
                break;
            case WITHDRAW:
                temp = new Withdraw(currentAccountNumber, screenDisplay, bankDatabase, keypad, cashDispenser);
                break;
            case DEPOSIT:
                temp = new Deposit(currentAccountNumber, screenDisplay, bankDatabase, keypad, depositSlot);
                break;
            case TRANSFER:
                temp = new Transfer(currentAccountNumber, screenDisplay, bankDatabase, keypad);
                break;
            case CHANGEPASSWORD:
                temp= new ChangePassword(currentAccountNumber, screenDisplay, bankDatabase, keypad);
                break;
            case VIEW_TRANSACTION_HISTORY:
                temp= new ViewTransactionHistory(currentAccountNumber, screenDisplay, bankDatabase);
                break;
        }
        return temp;
    }


}
