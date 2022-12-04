package Admin;

// user's account details
public class Account {
    // intialised variables to be used
    private String name;
    private int accountNumber;
    private int pin;
    private float availableBalance;
    private float totalBalance;

    // account constructor
    public Account(String name, int accountNumber, int pin, float availableBalance, float totalBalance) {
        this.name=name;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.availableBalance = availableBalance;
        this.totalBalance = totalBalance;
    }
    
    // validate user's account pin
    public boolean validatePin(int userPIN){
        if(userPIN == pin) {
            return true;
        }
        else
            return false;
    }

    // get user's account name
    public String getName(){
        return this.name;
    }

    // get user's account pin
    public int getPin(){
        return this.pin;
    }

    // user changes account pin number
    public void changePin(int newPin){
        this.pin=newPin;
    }

    // gets user's account total balance (cash + cheque (approved and not approved))
    public float getTotalBalance(){
        return totalBalance;
    }

    // gets user's account cash and approved cheque balance
    public float getAvailableBalance(){
        return availableBalance;
    }

    // add amount to user's account
    public void credit(float amount){ // amount is credited (added) to account
        // update cash and approved cheque balance
        availableBalance += amount;
        // update cash + approved and not approved cheque balance
        totalBalance += amount;
    }

    // add cheque amount user has deposited into their account
    public void creditCheque(float amount){ // not added to available balance because cheque needs to be approved by bank
        // add amount to cash + approved and not approved cheque balance
        totalBalance += amount;
    }

    // deduct amount from user's account
    public void debit(float amount){
        // update cash and approved cheque balance
        availableBalance -= amount;
        // update cash + approved and unapproved cheque balance
        totalBalance -= amount;
    }

    // add cheque value to balance
    public void creditApprovedCheque(float amount){
        availableBalance += amount;
    }

    // get user's account number
    public int getAccountNumber(){
        return accountNumber;
    }

}
