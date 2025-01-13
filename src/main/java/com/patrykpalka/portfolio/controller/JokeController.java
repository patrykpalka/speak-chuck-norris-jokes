package com.patrykpalka.portfolio.controller;

import com.patrykpalka.portfolio.service.ChuckNorrisJokesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jokes")
public class JokeController {

    private final ChuckNorrisJokesService jokesService;

    @Autowired
    public JokeController(ChuckNorrisJokesService jokesService) {
        this.jokesService = jokesService;
    }

    @GetMapping("/random")
    public String randomJoke() {
        return jokesService.getJoke();
    }
}
