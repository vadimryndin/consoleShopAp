package pl.coherentsolutions.core.exceptions;

public class ClientServerException extends Exception {
    public ClientServerException(String errorMessage, Throwable exception) {
        super(errorMessage, exception);
    }
}
