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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChuckNorrisJokesServiceTest {

    @InjectMocks
    private ChuckNorrisJokesService chuckNorrisJokesService;

    @Mock
    private VoiceRssService voiceRssService;

    @Mock
    private AudioPlaybackService audioPlaybackService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void shouldReturnJokeWhenApiIsAvailable() {
        // given
        String joke = "Chuck Norris can divide by zero.";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(joke);
        byte[] audioData = "audio".getBytes();

        when(restTemplate.getForEntity(
                "https://api.chucknorris.io/jokes/random",
                String.class
        )).thenReturn(responseEntity);
        when(voiceRssService.getVoiceRss(joke)).thenReturn(audioData);
        doNothing().when(audioPlaybackService).playAudioWithClip(audioData);

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJoke();

        // then
        Assertions.assertEquals(joke, response);
        verify(voiceRssService).getVoiceRss(joke);
        verify(audioPlaybackService).playAudioWithClip(audioData);
    }

    @Test
    void shouldReturnErrorMessageWhenApiCallFailsOrWhenJokeIsNull() {
        // given
        when(restTemplate.getForEntity(
                "https://api.chucknorris.io/jokes/random",
                String.class
        )).thenReturn(ResponseEntity.ok(null));

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJoke();

        // then
        Assertions.assertEquals("Could not get joke", response);
        verify(voiceRssService, never()).getVoiceRss(anyString());
        verify(audioPlaybackService, never()).playAudioWithClip(any());
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
    void shouldReturnNullWhenCategoriesApiCallFailsOrResponseIsNull() {
        // given
        when(restTemplate.exchange(
                "https://api.chucknorris.io/jokes/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        )).thenReturn(ResponseEntity.ok(null));

        // when
        List<String> response = chuckNorrisJokesService.getListOfCategories();

        // then
        Assertions.assertNull(response);
    }

    @Test
    void shouldReturnJokeFromCategoryWhenApiIsAvailable() {
        // given
        String joke = "Chuck Norris can't test for equality because he has no equal.";
        byte[] audioData = "audio".getBytes();

        when(restTemplate.getForEntity(
                "https://api.chucknorris.io/jokes/random?category=dev",
                String.class
        )).thenReturn(ResponseEntity.ok(joke));
        when(voiceRssService.getVoiceRss(joke)).thenReturn(audioData);
        doNothing().when(audioPlaybackService).playAudioWithClip(audioData);

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJokeByCategory("dev");

        // then
        Assertions.assertEquals(joke, response);
        verify(voiceRssService).getVoiceRss(joke);
        verify(audioPlaybackService).playAudioWithClip(audioData);
    }

    @Test
    void shouldReturnErrorMessageWhenCategoryApiCallFailsOrWhenJokeIsNull() {
        // given
        String errorMessage = "Could not get joke";

        when(restTemplate.getForEntity(
                "https://api.chucknorris.io/jokes/random?category=dev",
                String.class
        )).thenThrow(new RestClientException(errorMessage));

        // when
        String response = chuckNorrisJokesService.getAndPlayRandomJokeByCategory("dev");

        // then
        Assertions.assertEquals(errorMessage, response);
        verify(voiceRssService, never()).getVoiceRss(anyString());
        verify(audioPlaybackService, never()).playAudioWithClip(any());
    }
}
