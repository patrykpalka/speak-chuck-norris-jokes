package com.patrykpalka.portfolio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChuckNorrisJokesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChuckNorrisJokesService.class);
    private final RestTemplate restTemplate;
    private final VoiceRssService voiceRssService;
    private final AudioPlaybackService audioPlaybackService;

    @Autowired
    public ChuckNorrisJokesService(RestTemplate restTemplate, AudioPlaybackService audioPlaybackService, VoiceRssService voiceRssService) {
        this.restTemplate = restTemplate;
        this.voiceRssService = voiceRssService;
        this.audioPlaybackService = audioPlaybackService;
    }

    public String getAndPlayRandomJoke() {
        try {
            String apiUrl = "https://api.chucknorris.io/jokes/random";

            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            String joke = response.getBody();

            playJoke(joke);
            return joke;
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    private void playJoke(String joke) {
        if (joke != null) {
            byte[] audioData = voiceRssService.getVoiceRss(joke);
            audioPlaybackService.playAudioWithClip(audioData);
        } else {
            throw new RestClientException("Could not get joke");
        }
    }

    public List<String> getListOfCategories() {
        try {
            String apiUrl = "https://api.chucknorris.io/jokes/categories";

            ResponseEntity<List<String>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            List<String> categories = response.getBody();

            if (categories != null) {
                return categories;
            } else {
                throw new RestClientException("Could not get categories");
            }
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public String getAndPlayRandomJokeByCategory(String category) {
        try {
            String apiUrlFormat = "https://api.chucknorris.io/jokes/random?category=%s";

            ResponseEntity<String> response = restTemplate.getForEntity(String.format(apiUrlFormat, category), String.class);
            String joke = response.getBody();

            playJoke(joke);
            return joke;
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
}
