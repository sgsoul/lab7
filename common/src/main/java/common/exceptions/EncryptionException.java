package common.exceptions;

public class EncryptionException extends Exception {
    public EncryptionException() {
        super("Невозможно зашифровать.");
    }
}