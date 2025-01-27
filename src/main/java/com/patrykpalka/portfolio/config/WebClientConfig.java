package com.patrykpalka.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient jokesApiClient(WebClient.Builder builder) {
        return builder.baseUrl("https://api.chucknorris.io").build();
    }

    @Bean
    public WebClient voiceRssApiClient(WebClient.Builder builder) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(5 * 1024 * 1024))
                .build();

        return builder
                .baseUrl("https://api.voicerss.org")
                .exchangeStrategies(strategies)
                .build();
    }
}
