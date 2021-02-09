package io.ioxcorp.ioxbox.data.exceptions;

public class ExistingBoxException extends Exception {
    public ExistingBoxException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
