package com.patrykpalka.portfolio.service;

import com.patrykpalka.portfolio.exception.InvalidTextException;
import com.patrykpalka.portfolio.exception.VoiceRssApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoiceRssServiceTest {

    @InjectMocks
    private VoiceRssService voiceRssService;

    @Mock
    private WebClient voiceRssApiClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        lenient().when(voiceRssApiClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void shouldReturnAudioDataWhenApiIsAvailable() {
        byte[] audioData = "audio".getBytes();
        when(responseSpec.bodyToMono(byte[].class)).thenReturn(Mono.just(audioData));

        byte[] response = voiceRssService.getVoiceRss("test");

        assertArrayEquals(audioData, response);
    }

    @Test
    void shouldThrowExceptionWhenTextIsEmpty() {
        assertThrows(InvalidTextException.class, () -> voiceRssService.getVoiceRss(""));
    }

    @Test
    void shouldThrowExceptionWhenTextIsNull() {
        assertThrows(InvalidTextException.class, () -> voiceRssService.getVoiceRss(null));
    }

    @Test
    void shouldThrowExceptionWhenApiCallFails() {
        when(responseSpec.bodyToMono(byte[].class)).thenThrow(new WebClientException("API Error") {});

        assertThrows(VoiceRssApiException.class, () -> voiceRssService.getVoiceRss("test"));
    }

    @Test
    void shouldThrowExceptionForNullResponse() {
        when(responseSpec.bodyToMono(byte[].class)).thenReturn(Mono.justOrEmpty(null));

        assertThrows(VoiceRssApiException.class, () -> voiceRssService.getVoiceRss("test text"));
    }
}