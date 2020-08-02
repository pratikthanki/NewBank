package newbank.exceptions;

public class InsufficientFundsExceptions extends Exception {
    public InsufficientFundsExceptions() {
    }

    public InsufficientFundsExceptions(String message) {
        super(message);
    }
}
