package com.patrykpalka.portfolio.exception;

public class VoiceRssApiException extends RuntimeException {
    public VoiceRssApiException(String message) {
        super(message);
    }

    public VoiceRssApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
