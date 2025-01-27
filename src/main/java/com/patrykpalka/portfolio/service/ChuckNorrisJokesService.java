package com.patrykpalka.portfolio.service;

import com.patrykpalka.portfolio.dto.ChuckNorrisApiRandomJokeResponseDTO;
import com.patrykpalka.portfolio.exception.ChuckNorrisApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.List;

@Service
public class ChuckNorrisJokesService {

    private final WebClient jokesApiClient;
    private final VoiceRssService voiceRssService;
    private final AudioPlaybackService audioPlaybackService;

    @Autowired
    public ChuckNorrisJokesService(WebClient jokesApiClient, AudioPlaybackService audioPlaybackService, VoiceRssService voiceRssService) {
        this.jokesApiClient = jokesApiClient;
        this.voiceRssService = voiceRssService;
        this.audioPlaybackService = audioPlaybackService;
    }

    public String getAndPlayRandomJoke() {
        String joke = getJokeFromApi("/jokes/random");
        playJoke(joke);
        return joke;
    }

    public List<String> getListOfCategories() {
        try {
            String urlPath = "/jokes/categories";

            List<String> categories = jokesApiClient.get()
                    .uri(urlPath)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                    .block();

            if (categories == null || categories.isEmpty()) {
                throw new ChuckNorrisApiException("Failed to retrieve joke categories");
            }

            return categories;
        } catch (WebClientException e) {
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
                String.format("/jokes/random?category=%s", category)
        );
        playJoke(joke);
        return joke;
    }

    private String getJokeFromApi(String urlPath) {
        try {
            ChuckNorrisApiRandomJokeResponseDTO jokeBody = jokesApiClient.get()
                    .uri(urlPath)
                    .retrieve()
                    .bodyToMono(ChuckNorrisApiRandomJokeResponseDTO.class)
                    .block();

            if (jokeBody == null) {
                throw new ChuckNorrisApiException("Failed to retrieve joke from API");
            }

            return jokeBody.value();
        } catch (WebClientException e) {
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
