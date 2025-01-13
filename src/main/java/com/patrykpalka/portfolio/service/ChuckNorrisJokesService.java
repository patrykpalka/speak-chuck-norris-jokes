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
    private final String apiUrl = "https://api.chucknorris.io/jokes/random";

    @Autowired
    public ChuckNorrisJokesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getJoke() {
        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Map.class);
            Map<String, String> responseBody = response.getBody();
            return responseBody != null ? responseBody.get("value") : "No joke found!";
        } catch (RestClientException e) {
            return "No joke found!";
        }
    }
}
