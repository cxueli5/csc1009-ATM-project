package Admin;

// import java packages
import java.time.LocalDate;


// to get transaction info of accounts
public class TransactionInfo {
    // intialised variables
    private LocalDate dateOfTransaction;
    private int accountNumber;
    private double amount;
    private String transactionType;
    private String chequeNumber;
    private double totalBalance;
    private double availableBalance;
    // transaction information constructor
    public TransactionInfo(LocalDate dateOfTransaction, int accountNumber, String transactionType, double amount,  String chequeNumber, double totalBalance, double availableBalance){
        // initialization of variables
        this.dateOfTransaction = dateOfTransaction;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.chequeNumber = chequeNumber;
        this.totalBalance = totalBalance;
        this.availableBalance = availableBalance;
    }

    // method to get account number
    public int getAccountNumber(){
        return accountNumber;
    } 
    
    // method to get transaction type
    public String getTransactionType(){
        return transactionType;
    }

    // method to get cheque number
    public String getChequeNumber(){
        return chequeNumber;
    } 

    // method to get amount
    public double getAmount(){
        return amount;
    } 
    
    // method to get date of transaction
    public LocalDate getDateOfTransaction(){
        return dateOfTransaction;
    } 

    // method to get total balance
    public double getTotalBalance(){
        return totalBalance;
    } 

    // method to get available balance
    public double getAvailableBalance(){
        return availableBalance;
    }  
}
