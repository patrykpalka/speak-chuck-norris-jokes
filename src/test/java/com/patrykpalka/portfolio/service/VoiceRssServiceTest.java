package com.patrykpalka.portfolio.service;

import com.patrykpalka.portfolio.exception.InvalidTextException;
import com.patrykpalka.portfolio.exception.VoiceRssApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoiceRssServiceTest {

    @InjectMocks
    private VoiceRssService voiceRssService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void shouldReturnAudioDataWhenApiIsAvailable() {
        // given
        byte[] audioData = "audio".getBytes();
        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok(audioData);

        when(restTemplate.getForEntity(
                anyString(),
                eq(byte[].class)
        )).thenReturn(responseEntity);

        // when
        byte[] response = voiceRssService.getVoiceRss("test");

        // then
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
        // given
        when(restTemplate.getForEntity(
                anyString(),
                eq(byte[].class)
        )).thenThrow(new RestClientException("API Error"));

        // then
        assertThrows(VoiceRssApiException.class, () -> voiceRssService.getVoiceRss("test"));
    }

    @Test
    void shouldThrowExceptionForNullResponse() {
        // given
        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok(null);

        when(restTemplate.getForEntity(
                anyString(),
                eq(byte[].class)
        )).thenReturn(responseEntity);

        // then
        assertThrows(VoiceRssApiException.class, () -> voiceRssService.getVoiceRss("test text"));
    }
}
