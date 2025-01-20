package com.patrykpalka.portfolio.exception;

import com.patrykpalka.portfolio.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ErrorResponseDTO> createErrorResponse(
            String message,
            String path,
            HttpStatus status,
            String logPrefix,
            Exception e) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                message,
                LocalDateTime.now().toString(),
                path
        );
        LOGGER.error("{}: {} - Path: {}", logPrefix, message, path, e);
        return ResponseEntity
                .status(status)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(AudioDataEmptyException.class)
    public ResponseEntity<ErrorResponseDTO> handleAudioDataEmptyException(AudioDataEmptyException e, HttpServletRequest request) {
        return createErrorResponse(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                "Audio data error",
                e);
    }

    @ExceptionHandler(AudioPlaybackException.class)
    public ResponseEntity<ErrorResponseDTO> handleAudioPlaybackException(AudioPlaybackException e, HttpServletRequest request) {
        return createErrorResponse(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Audio playback error",
                e);
    }

    @ExceptionHandler(InvalidTextException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidTextException(InvalidTextException e, HttpServletRequest request) {
        return createErrorResponse(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                "Invalid text input",
                e);
    }

    @ExceptionHandler(VoiceRssApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleVoiceRssApiException(VoiceRssApiException e, HttpServletRequest request) {
        return createErrorResponse(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.SERVICE_UNAVAILABLE,
                "Voice RSS API error",
                e);
    }

    @ExceptionHandler(ChuckNorrisApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleChuckNorrisApiException(
            ChuckNorrisApiException e,
            HttpServletRequest request) {
        return createErrorResponse(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.SERVICE_UNAVAILABLE,
                "Chuck Norris API error",
                e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        return createErrorResponse(
                e.getMessage(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                "Illegal argument",
                e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpectedException(Exception e, HttpServletRequest request) {
        return createErrorResponse(
                "An unexpected error occurred",
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error",
                e);
    }
}
