package com.patrykpalka.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient jokesApiClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.chucknorris.io").build();
    }

    @Bean
    public WebClient voiceRssApiClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.voicerss.org").build();
    }
}
