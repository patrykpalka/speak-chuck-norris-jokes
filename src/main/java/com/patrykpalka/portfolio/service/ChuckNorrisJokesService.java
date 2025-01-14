package com.patrykpalka.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ChuckNorrisJokesService {

    private final RestTemplate restTemplate;
    private final VoiceRssService voiceRssService;
    private final AudioPlaybackService audioPlaybackService;
    private final String apiUrl = "https://api.chucknorris.io/jokes/random";

    @Autowired
    public ChuckNorrisJokesService(RestTemplate restTemplate, AudioPlaybackService audioPlaybackService, VoiceRssService voiceRssService) {
        this.restTemplate = restTemplate;
        this.voiceRssService = voiceRssService;
        this.audioPlaybackService = audioPlaybackService;
    }

    public String getJoke() {
        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Map.class);
            Map<String, String> responseBody = response.getBody();
            if (responseBody == null) return "No joke found!";
            String joke = responseBody.get("value");
            audioPlaybackService.playAudioWithClip(voiceRssService.getVoiceRss(joke));
            return joke;
        } catch (RestClientException e) {
            return "No joke found!";
        }
    }
}
