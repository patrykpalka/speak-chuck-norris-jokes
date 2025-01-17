package com.patrykpalka.portfolio.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
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
        String urlFormat = "https://api.voicerss.org/?key=%s&hl=%s&v=%s&c=%s&f=%s&src=%s";

        String key = "279b6ea964d74e8aa6ccb53a08593545";
        String language = "en-us";
        String voice = "Mary";
        String audioCodec = "WAV";
        String audioFormat = "44khz_16bit_stereo";

        String apiUrl = String.format(urlFormat, key, language, voice, audioCodec, audioFormat, "test");

        byte[] audioData = "audio".getBytes();
        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok(audioData);

        when(restTemplate.getForEntity(
                apiUrl,
                byte[].class
        )).thenReturn(responseEntity);

        // when
        byte[] response = voiceRssService.getVoiceRss("test");

        // then
        Assertions.assertArrayEquals(audioData, response);
    }

    @Test
    void shouldReturnNullWhenApiCallFails() {
        // given
        String urlFormat = "https://api.voicerss.org/?key=%s&hl=%s&v=%s&c=%s&f=%s&src=%s";

        String key = "279b6ea964d74e8aa6ccb53a08593545";
        String language = "en-us";
        String voice = "Mary";
        String audioCodec = "WAV";
        String audioFormat = "44khz_16bit_stereo";

        String apiUrl = String.format(urlFormat, key, language, voice, audioCodec, audioFormat, "test");

        when(restTemplate.getForEntity(
                apiUrl,
                byte[].class
        )).thenThrow(new RestClientException("API call failed"));

        // when
        byte[] response = voiceRssService.getVoiceRss("test");

        // then
        Assertions.assertNull(response);
    }
}
