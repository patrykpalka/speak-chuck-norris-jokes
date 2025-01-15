package com.patrykpalka.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ChuckNorrisJokesService {

    private final RestTemplate restTemplate;
    private final VoiceRssService voiceRssService;
    private final AudioPlaybackService audioPlaybackService;

    @Autowired
    public ChuckNorrisJokesService(RestTemplate restTemplate, AudioPlaybackService audioPlaybackService, VoiceRssService voiceRssService) {
        this.restTemplate = restTemplate;
        this.voiceRssService = voiceRssService;
        this.audioPlaybackService = audioPlaybackService;
    }

    public String getJoke() {
        try {
            String apiUrl = "https://api.chucknorris.io/jokes/random";

            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            String joke = response.getBody();

            if (joke != null) {
                audioPlaybackService.playAudioWithClip(voiceRssService.getVoiceRss(joke));
                return joke;
            } else {
                throw new RestClientException("Could not get joke");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
