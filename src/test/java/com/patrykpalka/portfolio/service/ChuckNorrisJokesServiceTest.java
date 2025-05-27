package com.patrykpalka.portfolio.service;

import com.patrykpalka.portfolio.dto.ChuckNorrisApiRandomJokeResponseDTO;
import com.patrykpalka.portfolio.exception.ChuckNorrisApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChuckNorrisJokesServiceTest {

    @Mock
    private VoiceRssService voiceRssService;

    @Mock
    private AudioPlaybackService audioPlaybackService;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ChuckNorrisJokesService chuckNorrisJokesService;

    @Test
    void shouldReturnJokeWhenApiIsAvailable() {
        // given
        String joke = "Chuck Norris can divide by zero.";
        byte[] audioData = "audio".getBytes();

        WebClient.RequestHeadersUriSpec uriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri("/jokes/random")).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);

        // Przygotuj mock DTO
        ChuckNorrisApiRandomJokeResponseDTO dto = mock(ChuckNorrisApiRandomJokeResponseDTO.class);
        when(dto.value()).thenReturn(joke);

        when(responseSpecMock.bodyToMono(ChuckNorrisApiRandomJokeResponseDTO.class)).thenReturn(Mono.just(dto));
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
        WebClient.RequestHeadersUriSpec uriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri("/jokes/random")).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(ChuckNorrisApiRandomJokeResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(500, "API Error", null, null, null)));

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

        WebClient.RequestHeadersUriSpec uriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri("/jokes/categories")).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(categories));

        // when
        List<String> response = chuckNorrisJokesService.getListOfCategories();

        // then
        assertEquals(categories, response);
    }

    @Test
    void shouldThrowExceptionWhenCategoriesApiCallFails() {
        // given
        WebClient.RequestHeadersUriSpec uriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri("/jokes/categories")).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.error(new WebClientResponseException(500, "API Error", null, null, null)));

        // then
        assertThrows(ChuckNorrisApiException.class, () -> {
            chuckNorrisJokesService.getListOfCategories();
        });
    }
}