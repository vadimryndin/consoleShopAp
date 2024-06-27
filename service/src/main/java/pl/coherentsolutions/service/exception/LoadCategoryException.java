package pl.coherentsolutions.service.exception;

public class LoadCategoryException extends Exception {
    public LoadCategoryException(String errorMessage, Throwable exception) {
        super(errorMessage, exception);
    }
}

