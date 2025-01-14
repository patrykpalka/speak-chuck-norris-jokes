package com.patrykpalka.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class VoiceRssService {

    private final RestTemplate restTemplate;
    private final String voiceRssApiUrlPrefix = "http://api.voicerss.org/?key=279b6ea964d74e8aa6ccb53a08593545&hl=en-us&v=Mary&c=WAV&f=44khz_16bit_stereo&src=";

    @Autowired
    public VoiceRssService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public byte[] getVoiceRss(String text) {
        try {
            String apiUrl = voiceRssApiUrlPrefix + text;

            ResponseEntity<byte[]> response = restTemplate.getForEntity(apiUrl, byte[].class);

            if (response.getBody() != null && response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RestClientException("Invalid response from Voice RSS API");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
