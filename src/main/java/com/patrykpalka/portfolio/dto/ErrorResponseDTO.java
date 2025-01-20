package com.patrykpalka.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing error message, timestamp and path")
public record ErrorResponseDTO(
        @Schema(description = "Error message", example = "Category is not valid")
        String message,
        @Schema(description = "Timestamp of the error", example = "2025-01-20T17:44:46.610191700")
        String timestamp,
        @Schema(description = "Path of the request", example = "/jokes/random")
        String path
) {
}
