package Admin;

// import java packages
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.IllegalFormatException;

public class BankDatabase {
    private static final int CANCELLED = 0;
    private static final String CANCELLED_CHEQUE = "0"; 

    // accounts database
    private ArrayList<Account> accounts = new ArrayList<Account>(); 
    private File accountsFile = new File(ACCOUNTS_DB_FILEPATH);
    // file path for AccountsDatabase.csv file
    private static final String ACCOUNTS_DB_FILEPATH = "Admin\\AccountsDatabase.csv";
    // file columns
    private static final int NAME_NUM_COL = 0;
    private static final int ACCOUNT_NUM_COL = 1;
    private static final int PIN_COL = 2;
    private static final int AVAILABLE_BALANCE_COL = 3;
    private static final int TOTAL_BALANCE_COL = 4;

    // cheque database
    private ArrayList<String> cheques = new ArrayList<String>();
    private File chequeFile = new File(CHEQUE_DB_FILEPATH);
    // file path for ChequeDatabase.csv file
    private static final String CHEQUE_DB_FILEPATH = "Admin\\ChequeDatabase.csv";
    // file columns
    private static final int CHEQUE_STRING_COL = 0;
    // array list of pending cheques
    private ArrayList<Float> chequesToClear = new ArrayList<Float>();

    
    // transaction database
    private ArrayList<TransactionInfo> transactions = new ArrayList<TransactionInfo>();
    private File transactionFile = new File(TRANSACTION_DB_FILEPATH);
    // file path for TransactionDatabase.csv file
    private static final String TRANSACTION_DB_FILEPATH = "Admin\\TransactionDatabase.csv";
    // file columns
    private static final int TRANSACTION_DATE_COL = 0;
    private static final int ACCOUNT_NUM_COL_TRX = 1;    
    private static final int TRANSACTION_TYPE_COL = 2;
    private static final int AMOUNT_COL = 3;
    private static final int CHEQUE_NUM_COL = 4;
    private static final int AVAILABLE_BALANCE_COL_TRX = 5;
    private static final int TOTAL_BALANCE_COL_TRX = 6;

    public BankDatabase(){
        // check if file exists first. if not, create database csv file
        if (!accountsFile.exists()) {
            createAccountsDatabaseCSV();
        }
        
        // db for accounts
        // if there isn't at least one record, create sample accounts & admin account
        // assume file always have header: 1 count, else read from database and populate accounts array
        if (getNoOfRecords(ACCOUNTS_DB_FILEPATH) <= 1) {
            // default admin account
            createAccountsRecord(new Account("admin", 111111, 111213, 0, 0));

            // create sample accounts
            // add to accounts Arraylist and csv
            createAccountsRecord(new Account("sam", 237942, 555238, 1000, 1000));
            createAccountsRecord(new Account("george", 338294, 328340, 2500, 2500));
            createAccountsRecord(new Account("sarah", 748250, 113634, 8000, 8000));
            createAccountsRecord(new Account("jake", 654786, 908786, 0, 0));
            createAccountsRecord(new Account("daryl", 723111, 487052, 0, 0));
            createAccountsRecord(new Account("john", 837934, 495840, 0, 0));
        } else {
            readAllAccountsRecords();
        }

        // db for cheques
        // create ChequeDatabase.csv if it does not exist
        if (!chequeFile.exists()) {
            createChequeDatabaseCSV(); 
        }

        if (getNoOfRecords(CHEQUE_DB_FILEPATH) <= 1) {
            createChequeRecord("BA8493");
            createChequeRecord("UE4938");
            createChequeRecord("FJ9458");
        } else {
            readAllChequeRecords();
        }
        
        // db for transactions
        // create TransactionDatabase.csv if it does not exist
        if (!transactionFile.exists()){
            createTransactionDatabaseCSV(); 
        }

        if (getNoOfRecords(TRANSACTION_DB_FILEPATH) <= 1){
            createTransactionRecord(new TransactionInfo(LocalDate.parse("2022-08-16"), 723111, "Withdraw", 500, "", 500, 1000));
            createTransactionRecord(new TransactionInfo(LocalDate.parse("2020-12-05"), 748250, "cashDeposit", 1000, "", 12000, 1000));
            createTransactionRecord(new TransactionInfo(LocalDate.parse("2021-01-26"), 654786, "Withdraw", 1200, "", 10000, 1000));
        } else {
            readAllTransactionRecords();
        }
    }

    /*******************************************
    *  create new accounts database csv file
    ********************************************/
    public void createAccountsDatabaseCSV(){
        try {
            FileWriter fileWriter = null;
            accountsFile.createNewFile();

            try {
                fileWriter = new FileWriter(ACCOUNTS_DB_FILEPATH, true);
                fileWriter.append("name,accountNumber,pin,availableBalance,totalBalance\n");
            } catch (IOException e) {
                System.out.println("createAccountsDatabaseCSV: Unable to write to file.");
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    System.out.println("createAccountsDatabaseCSV: Unable to close file.");
                }
            }
        } catch (IOException e) {
            System.out.println("createAccountsDatabaseCSV: File cannot be created.");
        }
    }

    /*******************************************
    *  create new cheques database csv file
    ********************************************/
    public void createChequeDatabaseCSV(){
        try {
            FileWriter fileWriter = null;
            chequeFile.createNewFile();

            try {
                fileWriter = new FileWriter(CHEQUE_DB_FILEPATH, true);
                fileWriter.append("chequeString\n");
            } catch (IOException e) {
                System.out.println("createChequeDatabaseCSV: Unable to write to file.");
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    System.out.println("createChequeDatabaseCSV: Unable to close file.");
                }
            }
        } catch (IOException e) {
            System.out.println("createChequeDatabaseCSV: File cannot be created.");
        }
    }
    
    /*******************************************
    *  create new transaction database csv file
    ********************************************/
    public void createTransactionDatabaseCSV(){
        try {
            FileWriter fileWriter = null;
            transactionFile.createNewFile();

            try {
                fileWriter = new FileWriter(TRANSACTION_DB_FILEPATH, true);
                fileWriter.append("date(YYYY-MM-DD),accountNumber,transactionType,amount,chequeNumber,availableBalance,totalBalance\n");
            } catch (IOException e) {
                System.out.println("createTransactionDatabaseCSV: Unable to write to file.");
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    System.out.println("createTransactionDatabaseCSV: Unable to close file.");
                }
            }
        } catch (IOException e) {
            System.out.println("createTransactionDatabaseCSV: File cannot be created.");
        }
    }

    /*******************************************
    *       get number of records in csv
    ********************************************/
    public int getNoOfRecords(String filePath){
        BufferedReader fileReader = null;
        int noOfRecords = 0;

        try {
            fileReader = new BufferedReader(new FileReader(filePath));

            String line;

            while((line = fileReader.readLine()) != null && line.length()>0) {
                noOfRecords++;
            }
        } catch (IOException e) {
            System.out.println("getNoOfRecords: Unable to read from file.");
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                System.out.println("getNoOfRecords: Unable to close file.");
            }
        }

        return noOfRecords;
    }

    /*************************************************
    * insert a single account into a new row in database
    **************************************************/
    public void createAccountsRecord(Account account) {
        FileWriter fileWriter = null;

        try {
            // append to file
            fileWriter = new FileWriter(ACCOUNTS_DB_FILEPATH, true);
            String stringToInsert = String.valueOf(account.getName()) + "," +
                                    String.valueOf(account.getAccountNumber()) + "," +
                                    String.valueOf(account.getPin()) + "," + 
                                    String.valueOf(account.getAvailableBalance()) + "," + 
                                    String.valueOf(account.getTotalBalance())
                                    + "\n";
            fileWriter.append(stringToInsert);

            accounts.add(accounts.size(), account);
        } catch (IOException e) {
            System.out.println("createAccountsRecord: Unable to write to file.");
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("createAccountsRecord: Unable to close file.");
            }
        }
    }

    /************************************************************
    * insert a single cheque string into a new row in database
    *************************************************************/
    public void createChequeRecord(String chequeString) {
        FileWriter fileWriter = null;

        try {
            // append to file
            fileWriter = new FileWriter(CHEQUE_DB_FILEPATH, true);
            String stringToInsert = chequeString + "\n";
            fileWriter.append(stringToInsert);

            cheques.add(cheques.size(), chequeString);
        } catch (IOException e) {
            System.out.println("createChequeRecord: Unable to write to file.");
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("createChequeRecord: Unable to close file.");
            }
        }
    }
    
    /************************************************************
    * insert a single transaction into a new row in database
    *************************************************************/
    public void createTransactionRecord(TransactionInfo transactionInfo) {
        FileWriter fileWriter = null;

        try {
            // append to file
            fileWriter = new FileWriter(TRANSACTION_DB_FILEPATH, true);
            String stringToInsert = String.valueOf(transactionInfo.getDateOfTransaction()) + "," +
                                    String.valueOf(transactionInfo.getAccountNumber()) + "," +
                                    String.valueOf(transactionInfo.getTransactionType()) + "," + 
                                    String.valueOf(transactionInfo.getAmount()) + "," + 
                                    String.valueOf(transactionInfo.getChequeNumber()) + "," +
                                    String.valueOf(transactionInfo.getTotalBalance()) + "," +
                                    String.valueOf(transactionInfo.getAvailableBalance())
                                    + "\n";
            fileWriter.append(stringToInsert);
            transactions.add(transactions.size(), transactionInfo);
        } catch (IOException e) {
            System.out.println("createTransactionRecord: Unable to write to file.");
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("createTransactionRecord: Unable to close file.");
            }
        }
    }

    /*************************************************************************
    * for initializing arrays and reading from accounts csv file at startup
    **************************************************************************/
    public void readAllAccountsRecords(){
        BufferedReader fileReader = null;

        try {
            fileReader = new BufferedReader(new FileReader(ACCOUNTS_DB_FILEPATH));

            String line;

            // consume first line (don't read header)
            fileReader.readLine();

            // initialize size of accounts array to number of records in DB
            // -1 ignore header count

            while((line = fileReader.readLine()) != null && line.length()>0) {
                String [] fields = line.split(",");

                try {
                    Account a = new Account(String.format(fields[NAME_NUM_COL]),
                            Integer.parseInt(fields[ACCOUNT_NUM_COL]),
                            Integer.parseInt(fields[PIN_COL]),
                            Float.parseFloat(fields[AVAILABLE_BALANCE_COL]),
                            Float.parseFloat(fields[TOTAL_BALANCE_COL]));

                    accounts.add(accounts.size(), a);

                    // when columns are not numeric
                    // when format string contains an illegal syntax
                } catch (NumberFormatException | IllegalFormatException e) {
                    System.out.println("readAllAccountsRecords: Wrong value type in file.");
                }
            }
        } catch (IOException e) {
            System.out.println("readAllAccountsRecords: Unable to read from file.");
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                System.out.println("readAllAccountsRecords: Unable to close file.");
            }
        }
    }

    /*************************************************************************
    * for initializing arrays and reading from cheque csv file at startup
    **************************************************************************/
    public void readAllChequeRecords(){
        BufferedReader fileReader = null;

        try {
            fileReader = new BufferedReader(new FileReader(CHEQUE_DB_FILEPATH));

            String line;

            // consume first line (don't read header)
            fileReader.readLine();

            while((line = fileReader.readLine()) != null && line.length()>0) {
                String [] fields = line.split(",");

                try {
                    cheques.add(cheques.size(), String.format(fields[CHEQUE_STRING_COL]));

                    // when format string contains an illegal syntax
                } catch (IllegalFormatException e) {
                    System.out.println("readAllChequeRecords: Wrong value type in file.");
                }
            }
        } catch (IOException e) {
            System.out.println("readAllChequeRecords: Unable to read from file.");
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                System.out.println("readAllChequeRecords: Unable to close file.");
            }
        }
    }
    
    /*************************************************************************
    * for initializing arrays and reading from transactions csv file at startup
    **************************************************************************/
    public void readAllTransactionRecords(){
        BufferedReader fileReader = null;

        try {
            fileReader = new BufferedReader(new FileReader(TRANSACTION_DB_FILEPATH));

            String line;

            // consume first line (don't read header)
            fileReader.readLine();

            // initialize size of transactions array to number of records in DB
            // -1 ignore header count
            while((line = fileReader.readLine()) != null && line.length()>0) {
                String [] fields = line.split(",");

                try {
                    TransactionInfo a = new TransactionInfo(LocalDate.parse(fields[TRANSACTION_DATE_COL]),
                            Integer.parseInt(fields[ACCOUNT_NUM_COL_TRX]),
                            String.format(fields[TRANSACTION_TYPE_COL]),
                            Float.parseFloat(fields[AMOUNT_COL]),
                            String.format(fields[CHEQUE_NUM_COL]),
                            Float.parseFloat(fields[AVAILABLE_BALANCE_COL_TRX]),
                            Float.parseFloat(fields[TOTAL_BALANCE_COL_TRX]));

                    transactions.add(transactions.size(), a);

                    // when columns are not numeric
                    // when format string contains an illegal syntax
                    // when date is not in correct format and cannot be parsed
                } catch (NumberFormatException | IllegalFormatException | DateTimeParseException e) {
                    System.out.println("readAllTransactionRecords: Wrong value type in file.");
                }
            }
        } catch (IOException e) {
            System.out.println("readAllTransactionRecords: Unable to read from file.");
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                System.out.println("readAllTransactionRecords: Unable to close file.");
            }
        }
    }

    /*************************************************
    * update bank database if there are any changes
    ***************************************************/
    public void updateAccountsDatabase() {
        File temp = new File("Admin\\temp.csv");
        accountsFile.renameTo(temp);

        // create new CSV
        createAccountsDatabaseCSV();
        // make copy of accounts arraylist
        ArrayList<Account> tempArrList = new ArrayList<Account>(accounts);
        accounts = new ArrayList<Account>();
        
        // go through currently updated accounts
        // write all accounts into new csv
        for (int i = 0; i < tempArrList.size(); i++) {
            //populate accounts arraylist and append to csv
            createAccountsRecord(tempArrList.get(i));
        }

        if (temp.exists()) {
            temp.delete();
        }
    }

    /*****************************************************
    *     return account object based on accountNumber
    ******************************************************/
    private Account getAccount(int accountNumber){
        for (Account currentAccount:accounts){
            if(currentAccount.getAccountNumber() == accountNumber){
                return currentAccount;
            }
        }
        return null;
    }

    /***********************************
    *     check if account exists
    ************************************/
    public boolean validateAccount(int accountNumber){
        for (Account currentAccount:accounts){
            if(currentAccount.getAccountNumber() == accountNumber){
                return true;
            }
        }
        return false;
    }

    /*********************************************************
    *     takes in input userPin, check with current accounts
    **********************************************************/
    public boolean authenticateUser(int userAccountNumber, int userPIN){

        // if userAccountNumber exists proceed to check pin number
        Account userAccount = getAccount(userAccountNumber);
        if(userAccount!=null){
            //check if actual account pin matches pin inputted
            return userAccount.validatePin(userPIN);
        }
        else{
            return false;
        }
    }
          
    /*********************************************************************
    *    for TransactionHistory class to get most recent 10 transactions
    **********************************************************************/
    public ArrayList<TransactionInfo> getTransactionHistory(int userAccountNumber){
        // search transactions[] for account number, starting from the back

        int transactionRecordCount = 0;
        ArrayList<TransactionInfo> transactionHistory = new ArrayList<TransactionInfo>();
        
        for(int i = transactions.size(); i > 0; i--){ 
            if(transactions.get(i-1).getAccountNumber() == userAccountNumber){
                transactionHistory.add(transactionRecordCount, transactions.get(i-1));
                transactionRecordCount++;
                if(transactionRecordCount == 10){
                    break;
                }
            }
        }
        return transactionHistory;
    }

    /*********************************************
    *    check with cheque String if it exists
    **********************************************/
    public boolean authenticateCheque(String chequeNumber){
        if(chequeNumber == CANCELLED_CHEQUE){
            return false;
        }
        return !(cheques.contains(chequeNumber));
    }

    /*************************************************
    *    add in cheque value into cheque arraylist
    **************************************************/
    public void recordCheque(String chequeNumber){
        createChequeRecord(chequeNumber);
    }

    /********************************************
    *    get account's cash + cheque balance
    **********************************************/
    public float getTotalBalance(int userAccountNumber){
        return getAccount(userAccountNumber).getTotalBalance();
    }

    /**************************************
    *    gets account's cash balance
    ***************************************/
    public float getAvailableBalance(int userAccountNumber){
        return getAccount(userAccountNumber).getAvailableBalance();
    }

    /***********************************************************************
    *    reduce account's availableBalance and totalBalance by amount
    ************************************************************************/
    public void debit(int userAccountNumber, float amount){
        getAccount(userAccountNumber).debit(amount);
        updateAccountsDatabase();
    }

    /********************************************************************
    *    add account's availableBalance and totalBalance by amount
    *********************************************************************/
    public void credit(int userAccountNumber, float amount){
        getAccount(userAccountNumber).credit(amount);
        updateAccountsDatabase();
    }

    /**************************************************
    *    add to account's totalbalance by amount
    ****************************************************/
    public void creditCheque(int userAccountNumber, float amount){
        getAccount(userAccountNumber).creditCheque(amount);
        chequesToClear.add(amount);
        updateAccountsDatabase();
    }
        
    /*****************************************************
    *    to simulate cheque deposits getting approved
    ******************************************************/
    public void approveCheques(int userAccountNumber){
        if(chequesToClear.size() > 0){
            while(chequesToClear.size() >0){
                getAccount(userAccountNumber).creditApprovedCheque(chequesToClear.get(0));
                // add the approved cheque's amount to the account's available balance
                chequesToClear.remove(0); // remove entry after approving the deposit
            }
        }
    }

    /*********************************************************
    *    update new pin number that user wants to change to
    **********************************************************/
    public void changePin(int userAccountNumber, int newPin){
        if (newPin != CANCELLED){
            getAccount(userAccountNumber).changePin(newPin);
            updateAccountsDatabase();
        }
    }
}
