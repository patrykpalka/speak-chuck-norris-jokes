package com.patrykpalka.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response containing available joke categories")
public record CategoryResponseDTO(
        @Schema(description = "List of available joke categories", example = "[\"animal\", \"career\", \"celebrity\"]")
        List<String> categories
) {
}
