package Hardware;

// import java packages
import java.text.DecimalFormat;

// ATM's screen display
public class ScreenDisplay {
    // display amount in proper money format
    private static final DecimalFormat df = new DecimalFormat("$0.00");

    // display one line message
    public void displayMessage(String message) {
        System.out.print(message);
    }

    // display message line by line
    public void displayMessageLine(String message) {
        System.out.println(message);
    }

    // display money amount on screen
    public void displayDollarAmount(double amount) {
        System.out.print(df.format(amount));
    }
}
