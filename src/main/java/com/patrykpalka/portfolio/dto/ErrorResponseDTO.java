package com.patrykpalka.portfolio.dto;

public record ErrorResponseDTO(
        String message,
        String timestamp,
        String path
) {
}
