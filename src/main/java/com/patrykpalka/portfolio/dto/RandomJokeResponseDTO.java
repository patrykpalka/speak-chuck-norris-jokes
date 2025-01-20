package com.patrykpalka.portfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing a random joke")
public record RandomJokeResponseDTO(
        @Schema(description = "Random joke", example = "Chuck Norris can divide by zero.")
        String joke
) {
}
