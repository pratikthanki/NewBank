package newbank.database.exception;

public class NewBankDatabase extends RuntimeException {
    public NewBankDatabase(String message, Exception e) {
        super(message, e);
    }
}
