package com.patrykpalka.portfolio.service;

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
        try {
            String urlFormat = "https://api.voicerss.org/?key=%s&hl=%s&v=%s&c=%s&f=%s&src=%s";

            String key = "279b6ea964d74e8aa6ccb53a08593545";
            String language = "en-us";
            String voice = "Mary";
            String audioCodec = "WAV";
            String audioFormat = "44khz_16bit_stereo";

            String apiUrl = String.format(urlFormat, key, language, voice, audioCodec, audioFormat, text);

            ResponseEntity<byte[]> response = restTemplate.getForEntity(apiUrl, byte[].class);
            byte[] responseBody = response.getBody();

            if (responseBody != null && response.getStatusCode().is2xxSuccessful()) {
                return responseBody;
            } else {
                throw new RestClientException("Invalid response from Voice RSS API");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
