package pl.coherentsolutions.service.exception;

public class ConfigReaderException extends Exception {
    public ConfigReaderException(String errorMessage, Throwable exception) {
        super(errorMessage, exception);
    }
}
