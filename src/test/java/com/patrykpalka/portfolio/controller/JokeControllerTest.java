package com.patrykpalka.portfolio.controller;

import com.patrykpalka.portfolio.dto.CategoryResponseDTO;
import com.patrykpalka.portfolio.dto.RandomJokeResponseDTO;
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
        ResponseEntity<RandomJokeResponseDTO> response = restTemplate.getForEntity(
                getBaseUrl() + "/random",
                RandomJokeResponseDTO.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().joke()).isEqualTo(expectedJoke);
    }

    @Test
    void shouldReturnJokeByCategory() {
        // given
        String category = "dev";
        String expectedJoke = "Chuck Norris writes code that optimizes itself.";
        when(jokesService.getAndPlayRandomJokeByCategory(category)).thenReturn(expectedJoke);

        // when
        ResponseEntity<RandomJokeResponseDTO> response = restTemplate.getForEntity(
                getBaseUrl() + "/random?category=" + category,
                RandomJokeResponseDTO.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().joke()).isEqualTo(expectedJoke);
    }

    @Test
    void shouldReturnListOfCategories() {
        // given
        List<String> expectedCategories = Arrays.asList("dev", "science", "sport");
        when(jokesService.getListOfCategories()).thenReturn(expectedCategories);

        // when
        ResponseEntity<CategoryResponseDTO> response = restTemplate.getForEntity(
                getBaseUrl() + "/categories",
                CategoryResponseDTO.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().categories())
                .isNotEmpty()
                .hasSize(3)
                .containsExactlyElementsOf(expectedCategories);
    }
}
