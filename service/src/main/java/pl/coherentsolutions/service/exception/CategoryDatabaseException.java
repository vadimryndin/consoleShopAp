package pl.coherentsolutions.service.exception;

public class CategoryDatabaseException extends Exception {
    public CategoryDatabaseException(String errorMessage, Throwable exception) {
        super(errorMessage, exception);
    }
}
