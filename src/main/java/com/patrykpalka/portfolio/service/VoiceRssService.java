package com.patrykpalka.portfolio.service;

import com.patrykpalka.portfolio.exception.InvalidTextException;
import com.patrykpalka.portfolio.exception.VoiceRssApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
public class VoiceRssService {

    private final WebClient voiceRssApiClient;

    @Autowired
    public VoiceRssService(WebClient voiceRssApiClient) {
        this.voiceRssApiClient = voiceRssApiClient;
    }

    public byte[] getVoiceRss(String text) {
        String apiUrlPath = apiUrlPath(text);

        try {
            return voiceRssApiClient.get()
                    .uri(apiUrlPath)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (WebClientException e) {
            throw new VoiceRssApiException("Failed to communicate with Voice RSS API", e);
        }
    }

    private String apiUrlPath(String text) {
        if (text == null || text.isEmpty()) {
            throw new InvalidTextException("Text cannot be null or empty");
        }

        String urlFormat = "/?key=%s&hl=%s&v=%s&c=%s&f=%s&src=%s";

        String key = "279b6ea964d74e8aa6ccb53a08593545";
        String language = "en-us";
        String voice = "Mary";
        String audioCodec = "WAV";
        String audioFormat = "44khz_16bit_stereo";

        return String.format(urlFormat, key, language, voice, audioCodec, audioFormat, text);
    }
}
