package Hardware;

// imported java packages
import java.util.Scanner;

// ATM's physical keypad
public class Keypad {

    // intialised variables to be used
    private Scanner input;
    private ScreenDisplay screenDisplay;

    // constants to declare input type (large numbers so we don't clash with other already-declared constants)
    private static final String CHEQUE_FORMAT = "cheque";
    private static final String DOLLAR_CENTS_FORMAT = "dollarcents";
    private static final String INTEGER_FORMAT = "integer";
    private static final String NAME_FORMAT = "name";
    private static final String CANCELLED = "0"; // string type because use string scanner, next(), for all inputs

    // pattern specifiers for the input data
    final String DOLLAR_CENTS_PATTERN = "[0-9]+[\\.]{0,1}[0-9]{0,2}"; // only numbers and 1 decimal point. 0 to 2 decimal places allowed
    final String INTEGER_PATTERN = "[0-9]+"; // any whole number
    final String CHEQUE_PATTERN = "[A-Z]{2}[0-9]{4}+"; // 2 uppercase letter & 4 digits
    final String NAME_PATTERN = "[A-Z][a-z]+"; // any number of alphabets, no special characters, start with uppercase letter
    final String CANCELLED_PATTERN = "[0]";

    // error messages to print if user inputs the wrong data type/format
    final String DOLLAR_CENTS_MSG = "\nOnly positive numbers allowed, up to 2 decimal places (for example, 50, 50.0, or 50.00). \nTry again(enter 0 to cancel): "; 
    final String INTEGER_MSG = "\nOnly positive numbers allowed, no decimal places. \nTry again (enter 0 to cancel): ";
    final String CHEQUE_ERROR_MSG = "\nOnly uppercase letters and numbers allowed (for example, AB1234). \nTry again (enter 0 to cancel): ";
    final String NAME_ERROR_MSG = "\nOnly letters allowed, start with a capital letter (for example, James). \nTry again (enter 0 to cancel): ";


    // keypad constructor
    public Keypad(ScreenDisplay ATMScreenDisplay){
        input = new Scanner(System.in);
        screenDisplay = ATMScreenDisplay;
    }
    
    boolean validInput = false;
    int inputNumber = 1; 
    String userInput = "";
    String patternToUse = "";
    String errorMessageToUse = "";

    public float getNumericInput(String patternSpecifier) throws IllegalArgumentException{
        // exception used to catch programming errors
        if (!patternSpecifier.equals(INTEGER_FORMAT) && !patternSpecifier.equals(DOLLAR_CENTS_FORMAT)){  
            throw new IllegalArgumentException("\nError at getNumericInput(pattern), wrong pattern specifier");
        }
        // need different return types (float vs String), so reusability is limited, 
        // hence used separate functions for numeric & alphanumeric inputs
        // however, this function is useable for both integer and float inputs

        switch(patternSpecifier){   //based on calling function's needs
            case DOLLAR_CENTS_FORMAT:
                patternToUse = DOLLAR_CENTS_PATTERN;
                errorMessageToUse = DOLLAR_CENTS_MSG;
                break;
            case INTEGER_FORMAT:  
                patternToUse = INTEGER_PATTERN;
                errorMessageToUse = INTEGER_MSG;
                break;
        }
        
        while(!validInput){
            if(inputNumber != 1)    // prompt for another input if it's not the first time asking for input
            {
                screenDisplay.displayMessage(errorMessageToUse); // display custom error message, specified above
            }
            userInput = input.next();

            if(userInput.matches(patternToUse) || userInput == CANCELLED){    // pattern to use: specified above
                validInput = true;
            }            
            inputNumber = 0; // will print error message to prompt for valid inputs
        }
        // reset shared variables, return input to caller 
        inputNumber = 1;
        validInput = false;
        return Float.parseFloat(userInput);
    }

    // overloading of getNumericInput, but specifies number of digits user should input
    public int getNumericInput(String patternSpecifier, int i) throws IllegalArgumentException{
        if (!patternSpecifier.equals(INTEGER_FORMAT)){  
            throw new IllegalArgumentException("\nError, Wrong pattern specifier for getNumericInput(pattern, i)");
        }
        // function is usable only for integer inputs with length requirements

        // customised pattern and message for the number of digits requested (i)
        String integerPatternWithSize = "[0-9]{" + i +"}"; // any whole number, must have i digits (i is specified by caller)
        String integerMsgWithSize = "\nOnly positive numbers allowed, no decimal places. Must input " + i + " digits. \nTry again (enter 0 to cancel): ";
        
        if(patternSpecifier == INTEGER_FORMAT){ 
            patternToUse = integerPatternWithSize;
            errorMessageToUse = integerMsgWithSize;
        }
        
        while(!validInput){
            if(inputNumber != 1)    // prompt for another input if it's not the first time asking for input
            {
                screenDisplay.displayMessage(errorMessageToUse); // display custom error message, specified above
            }
            userInput = input.next(); // scan user input

            if(userInput.matches(patternToUse)){    // user inputs i digits, or enters 0
                validInput = true;
            }            
            else if(userInput.matches(CANCELLED_PATTERN)){
                validInput = true;
            }
            inputNumber = 0; // will print error message to prompt for valid inputs
        }
        // reset shared variables, return input to caller 
        inputNumber = 1;
        validInput = false;
        return Integer.parseInt(userInput);
    }

    // get user's alphanumeric input on keypad
    public String getAlphanumericInput(String patternSpecifier){
        if(!patternSpecifier.equals(NAME_FORMAT) && !patternSpecifier.equals(CHEQUE_FORMAT))
        {
            throw new IllegalArgumentException("\nWrong pattern specifier for getAlphanumericInput(pattern, i)");
        }
        
        // get input type with requirements based on calling function's needs
        switch(patternSpecifier){   
            case CHEQUE_FORMAT:
                patternToUse = CHEQUE_PATTERN;
                errorMessageToUse = CHEQUE_ERROR_MSG;
                break;
            case NAME_FORMAT:
                patternToUse = NAME_PATTERN;
                errorMessageToUse = NAME_ERROR_MSG;
                break;
        }

        while(!validInput){
            if(inputNumber != 1)    // prompt for another input if it's not the first time asking for input
            {
                screenDisplay.displayMessage(errorMessageToUse); // display custom error message, specified above
            }
            userInput = input.next();
            if(userInput.matches(patternToUse) || userInput.equals(CANCELLED)){
                validInput = true;
            }            
            inputNumber = 0; // will print error message to prompt for valid inputs
        }
        // reset shared variables, return input to caller 
        inputNumber = 1;
        validInput = false;
        return userInput;
    }    
}
