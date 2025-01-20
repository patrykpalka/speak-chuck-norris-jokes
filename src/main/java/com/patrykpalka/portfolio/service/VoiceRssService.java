package com.patrykpalka.portfolio.service;

import com.patrykpalka.portfolio.exception.InvalidTextException;
import com.patrykpalka.portfolio.exception.VoiceRssApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class VoiceRssService {

    private final RestTemplate restTemplate;

    @Autowired
    public VoiceRssService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public byte[] getVoiceRss(String text) {
        String apiUrl = getApiUrl(text);

        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(apiUrl, byte[].class);
            byte[] responseBody = response.getBody();

            if (responseBody == null || !response.getStatusCode().is2xxSuccessful()) {
                throw new VoiceRssApiException("Invalid response from Voice RSS API");
            }

            return responseBody;
        } catch (RestClientException e) {
            throw new VoiceRssApiException("Failed to communicate with Voice RSS API", e);
        }
    }

    private String getApiUrl(String text) {
        if (text == null || text.isEmpty()) {
            throw new InvalidTextException("Text cannot be null or empty");
        }

        String urlFormat = "https://api.voicerss.org/?key=%s&hl=%s&v=%s&c=%s&f=%s&src=%s";

        String key = "279b6ea964d74e8aa6ccb53a08593545";
        String language = "en-us";
        String voice = "Mary";
        String audioCodec = "WAV";
        String audioFormat = "44khz_16bit_stereo";

        return String.format(urlFormat, key, language, voice, audioCodec, audioFormat, text);
    }
}
