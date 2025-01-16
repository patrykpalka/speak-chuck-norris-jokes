package com.patrykpalka.portfolio.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChuckNorrisJokesServiceTest {

    @InjectMocks
    private ChuckNorrisJokesService chuckNorrisJokesService;

    @Mock
    private AudioPlaybackService audioPlaybackService;

    @Mock
    private VoiceRssService voiceRssService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void shouldReturnJokeWhenApiIsAvailable() {
        // given
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Chuck Norris can divide by zero.");

        when(restTemplate.getForEntity(
                "https://api.chucknorris.io/jokes/random",
                String.class
        )).thenReturn(responseEntity);

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJoke();

        // then
        Assertions.assertEquals("Chuck Norris can divide by zero.", response);
    }

    @Test
    void shouldReturnNoJokeFoundWhenApiCallFails() {
        // given
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Could not get joke");

        when(restTemplate.getForEntity(
                "https://api.chucknorris.io/jokes/random",
                String.class
        )).thenReturn(responseEntity);

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJoke();

        // then
        Assertions.assertEquals("Could not get joke", response);
    }

    @Test
    void shouldReturnListOfCategories() {
        // given
        List<String> categories = List.of(
                "animal","career","celebrity","dev","explicit","fashion","food","history",
                "money","movie","music","political","religion","science","sport","travel");
        ResponseEntity<List<String>> responseEntity = ResponseEntity.ok(categories);

        when(restTemplate.exchange(
                "https://api.chucknorris.io/jokes/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        )).thenReturn(responseEntity);

        // when
        List<String> response = chuckNorrisJokesService.getListOfCategories();

        // then
        Assertions.assertEquals(categories, response);
    }

    @Test
    void shouldReturnNullWhenApiCallFails() {
        // given
        ResponseEntity<List<String>> responseEntity = ResponseEntity.ok(null);

        when(restTemplate.exchange(
                "https://api.chucknorris.io/jokes/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        )).thenReturn(responseEntity);

        // when
        List<String> response = chuckNorrisJokesService.getListOfCategories();

        // then
        Assertions.assertNull(response);
    }

    @Test
    void shouldReturnJokeFromCategoryWhenApiIsAvailable() {
        // given
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Chuck Norris can't test for equality because he has no equal.");

        when(restTemplate.getForEntity(
                "https://api.chucknorris.io/jokes/random?category=dev",
                String.class
        )).thenReturn(responseEntity);

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJokeByCategory("dev");

        // then
        Assertions.assertEquals("Chuck Norris can't test for equality because he has no equal.", response);
    }

    @Test
    void shouldReturnErrorMessageWhenApiCallFails() {
        // given
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Could not get joke");

        when(restTemplate.getForEntity(
                "https://api.chucknorris.io/jokes/random?category=dev",
                String.class
        )).thenReturn(responseEntity);

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJokeByCategory("dev");

        // then
        Assertions.assertEquals("Could not get joke", response);
    }
}
