package com.patrykpalka.portfolio.controller;

import com.patrykpalka.portfolio.service.ChuckNorrisJokesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jokes")
public class JokeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JokeController.class);
    private final ChuckNorrisJokesService jokesService;

    @Autowired
    public JokeController(ChuckNorrisJokesService jokesService) {
        this.jokesService = jokesService;
    }

    @GetMapping("/random")
    public String randomJoke() {
        String joke = jokesService.getAndPlayJoke();
        LOGGER.debug("randomJoke() returns: {}", joke);
        return joke;
    }

    @GetMapping("/categories")
    public List<String> categories() {
        List<String> categories = jokesService.getListOfCategories();
        LOGGER.debug("categories() returns: {}", categories);
        return categories;
    }
}
