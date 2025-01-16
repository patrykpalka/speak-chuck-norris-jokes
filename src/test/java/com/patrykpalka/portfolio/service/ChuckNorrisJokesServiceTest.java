package com.patrykpalka.portfolio.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChuckNorrisJokesServiceTest {

    @InjectMocks
    private ChuckNorrisJokesService chuckNorrisJokesService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void shouldReturnJokeWhenApiIsAvailable() {
        // given
        Map<String, String> apiResponse = Map.of("value", "Chuck Norris can divide by zero.");
        ResponseEntity<Map> responseEntity = ResponseEntity.ok(apiResponse);

        when(restTemplate.exchange(
                "https://api.chucknorris.io/jokes/random",
                HttpMethod.GET,
                null,
                Map.class
        )).thenReturn(responseEntity);

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJoke();

        // then
        Assertions.assertEquals("Chuck Norris can divide by zero.", response);
    }

    @Test
    void shouldReturnNoJokeFoundWhenApiReturnsNull() {
        // given
        ResponseEntity<Map> responseEntity = ResponseEntity.ok(null);

        when(restTemplate.exchange(
                "https://api.chucknorris.io/jokes/random",
                HttpMethod.GET,
                null,
                Map.class
        )).thenReturn(responseEntity);

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJoke();

        // then
        Assertions.assertEquals("No joke found!", response);
    }

    @Test
    void shouldReturnNoJokeFoundWhenApiCallFails() {
        // given
        when(restTemplate.exchange(
                "https://api.chucknorris.io/jokes/random",
                HttpMethod.GET,
                null,
                Map.class
        )).thenThrow(new RestClientException("API is down") {});

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJoke();

        // then
        Assertions.assertEquals("No joke found!", response);
    }
}
