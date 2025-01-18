package com.patrykpalka.portfolio.controller;

import com.patrykpalka.portfolio.service.ChuckNorrisJokesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JokeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private ChuckNorrisJokesService jokesService;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/jokes";
    }

    @Test
    void shouldReturnRandomJoke() {
        // given
        String expectedJoke = "Chuck Norris can divide by zero.";
        when(jokesService.getAndPlayRandomJoke()).thenReturn(expectedJoke);

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/random",
                String.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedJoke);
    }

    @Test
    void shouldReturnJokeByCategory() {
        // given
        String category = "dev";
        String expectedJoke = "Chuck Norris writes code that optimizes itself.";
        when(jokesService.getAndPlayRandomJokeByCategory(category)).thenReturn(expectedJoke);

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/random?category=" + category,
                String.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedJoke);
    }

    @Test
    void shouldReturnListOfCategories() {
        // given
        List<String> expectedCategories = Arrays.asList("dev", "science", "sport");
        when(jokesService.getListOfCategories()).thenReturn(expectedCategories);

        // when
        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/categories",
                List.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()).hasSize(3);
    }

    @Test
    void shouldHandleInvalidCategory() {
        // given
        String invalidCategory = "invalid";
        String expectedJoke = "No joke found for this category";
        when(jokesService.getAndPlayRandomJokeByCategory(invalidCategory)).thenReturn(expectedJoke);

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/random?category=" + invalidCategory,
                String.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedJoke);
    }
}
