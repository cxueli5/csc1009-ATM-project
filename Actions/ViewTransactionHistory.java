package Actions;
//files imported from other packages
import Admin.BankDatabase;
import Admin.TransactionInfo;
import Hardware.ScreenDisplay;

import java.util.ArrayList;
// view transaction history
public class ViewTransactionHistory extends Transaction{
    // view transaction history constructor
    public ViewTransactionHistory(int userAccountNumber, ScreenDisplay atmScreenDisplay, BankDatabase atmBankDatabase){
        super(userAccountNumber, atmScreenDisplay, atmBankDatabase);
    }
    // execute view transaction history
    public void execute(){
        BankDatabase bankDatabase = getBankDatabase();
        ScreenDisplay screenDisplay = getScreenDisplay();
        ArrayList<TransactionInfo> transactionHistory = bankDatabase.getTransactionHistory(getAccountNumber());
        
        //initialize number of transactions
        int numberOfTransactions = transactionHistory.size();
        
        if (numberOfTransactions >0) {
            screenDisplay.displayMessageLine("  Date   |  Transaction  | Amount | Cheque Number | Available Balance | Total Balance");
            // for every transaction, print out date and amount
            for(int i = 0; i<numberOfTransactions; i++){
                screenDisplay.displayMessage("\n" + transactionHistory.get(i).getDateOfTransaction().toString() + ", ");
                screenDisplay.displayMessage(transactionHistory.get(i).getTransactionType() + ", ");
                screenDisplay.displayDollarAmount(transactionHistory.get(i).getAmount());
                // check if got cheque number, if have print out in transaction history
                if (transactionHistory.get(i).getChequeNumber() != "") {
                    screenDisplay.displayMessage(", "+ transactionHistory.get(i).getChequeNumber() + ", ");}
                else {
                    screenDisplay.displayMessage(",    NA   , ");
                }
                // display available and total balance
                screenDisplay.displayDollarAmount(transactionHistory.get(i).getAvailableBalance());
                screenDisplay.displayMessage(", ");
                screenDisplay.displayDollarAmount(transactionHistory.get(i).getTotalBalance());
            }
        }
        // if there is no transaction at all
        else {
            screenDisplay.displayMessageLine("No previous transactions.");
        }
        
    }
}
