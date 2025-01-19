package com.patrykpalka.portfolio.service;

import com.patrykpalka.portfolio.exception.ChuckNorrisApiException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertEquals(joke, response);
        verify(voiceRssService).getVoiceRss(joke);
        verify(audioPlaybackService).playAudioWithClip(audioData);
    }

    @Test
    void shouldThrowExceptionWhenApiCallFails() {
        // given
        when(restTemplate.getForEntity(
                "https://api.chucknorris.io/jokes/random",
                String.class
        )).thenThrow(new RestClientException("API Error"));

        // then
        assertThrows(ChuckNorrisApiException.class, () -> {
            chuckNorrisJokesService.getAndPlayRandomJoke();
        });

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
        assertEquals(categories, response);
    }

    @Test
    void shouldThrowExceptionWhenCategoriesApiCallFails() {
        // given
        when(restTemplate.exchange(
                "https://api.chucknorris.io/jokes/categories",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        )).thenThrow(new RestClientException("API Error"));

        // then
        assertThrows(ChuckNorrisApiException.class, () -> {
            chuckNorrisJokesService.getListOfCategories();
        });
    }
}
