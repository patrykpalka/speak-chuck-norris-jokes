package com.patrykpalka.portfolio.service;

import com.patrykpalka.portfolio.exception.ChuckNorrisApiException;
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
        String joke = getJokeFromApi("https://api.chucknorris.io/jokes/random");
        playJoke(joke);
        return joke;
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

            if (response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
                throw new ChuckNorrisApiException("Failed to retrieve joke categories");
            }

            return response.getBody();
        } catch (RestClientException e) {
            throw new ChuckNorrisApiException("Failed to communicate with Chuck Norris API", e);
        }
    }

    public String getAndPlayRandomJokeByCategory(String category) {
        List<String> validCategories = getListOfCategories();

        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        } else if (!validCategories.contains(category)) {
            throw new IllegalArgumentException("Category is not valid");
        }

        String joke = getJokeFromApi(
                String.format("https://api.chucknorris.io/jokes/random?category=%s", category)
        );
        playJoke(joke);
        return joke;
    }

    private String getJokeFromApi(String apiUrl) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            if (response.getBody() == null || !response.getStatusCode().is2xxSuccessful()) {
                throw new ChuckNorrisApiException("Failed to retrieve joke from API");
            }

            return response.getBody();
        } catch (RestClientException e) {
            throw new ChuckNorrisApiException("Failed to communicate with Chuck Norris API", e);
        }
    }

    private void playJoke(String joke) {
        if (joke == null || joke.isEmpty()) {
            throw new ChuckNorrisApiException("Received empty joke from API");
        }

        byte[] audioData = voiceRssService.getVoiceRss(joke);
        audioPlaybackService.playAudioWithClip(audioData);
    }
}
