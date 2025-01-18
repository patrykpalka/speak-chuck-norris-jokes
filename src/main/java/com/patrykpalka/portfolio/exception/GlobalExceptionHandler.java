package com.patrykpalka.portfolio.exception;

import com.patrykpalka.portfolio.dto.ErrorResponseDTO;
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

    @ExceptionHandler(AudioDataEmptyException.class)
    public ResponseEntity<ErrorResponseDTO> handleAudioDataEmptyException(AudioDataEmptyException e) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                e.getMessage(),
                LocalDateTime.now()

        );
        LOGGER.error(errorResponseDTO.toString(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }

    @ExceptionHandler(AudioPlaybackException.class)
    public ResponseEntity<ErrorResponseDTO> handleAudioPlaybackException(AudioPlaybackException e) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                e.getMessage(),
                LocalDateTime.now()

        );
        LOGGER.error(errorResponseDTO.toString(), e.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }
}
