package com.patrykpalka.portfolio.exception;

public class ChuckNorrisApiException extends RuntimeException {
    public ChuckNorrisApiException(String message) {
        super(message);
    }

    public ChuckNorrisApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
