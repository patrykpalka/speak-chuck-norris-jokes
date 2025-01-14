package com.patrykpalka.portfolio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpeakChuckNorrisJokesApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeakChuckNorrisJokesApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Starting Speak Chuck Norris Jokes Application");
        SpringApplication.run(SpeakChuckNorrisJokesApplication.class, args);
        LOGGER.info("Speak Chuck Norris Jokes Application started successfully");
    }
}
