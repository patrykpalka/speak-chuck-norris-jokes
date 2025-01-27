package com.patrykpalka.portfolio.dto;

public record ChuckNorrisApiRandomJokeResponseDTO(
        String icon_url,
        String id,
        String url,
        String value
) {
}
