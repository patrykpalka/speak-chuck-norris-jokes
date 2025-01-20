package com.patrykpalka.portfolio.controller;

import com.patrykpalka.portfolio.dto.CategoryResponseDTO;
import com.patrykpalka.portfolio.dto.ErrorResponseDTO;
import com.patrykpalka.portfolio.dto.RandomJokeResponseDTO;
import com.patrykpalka.portfolio.service.ChuckNorrisJokesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jokes")
@Tag(name = "Jokes", description = "Chuck Norris Jokes API with text-to-speech capabilities")
public class JokeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JokeController.class);
    private final ChuckNorrisJokesService jokesService;

    @Autowired
    public JokeController(ChuckNorrisJokesService jokesService) {
        this.jokesService = jokesService;
    }

    @GetMapping("/random")
    @Operation(summary = "Get a random joke", description = "Retrieves and plays a random Chuck Norris joke")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RandomJokeResponseDTO.class))}),
            @ApiResponse(responseCode = "503", description = "Chuck Norris external API difficulties",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))})
    })
    public ResponseEntity<RandomJokeResponseDTO> randomJoke(
            @Parameter(description = "Category of the joke", example = "dev")
            @RequestParam(required = false) String category) {
        String joke;

        if (category == null) {
            joke = jokesService.getAndPlayRandomJoke();
        } else {
            joke = jokesService.getAndPlayRandomJokeByCategory(category);
        }

        RandomJokeResponseDTO randomJokeResponseDTO = new RandomJokeResponseDTO(joke);
        LOGGER.debug("randomJoke() returns: {}", randomJokeResponseDTO);

        return ResponseEntity.ok(randomJokeResponseDTO);
    }

    @GetMapping("/categories")
    public ResponseEntity<CategoryResponseDTO> categories() {
        List<String> categories = jokesService.getListOfCategories();

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO(categories);
        LOGGER.debug("categories() returns: {}", categoryResponseDTO);

        return ResponseEntity.ok(categoryResponseDTO);
    }
}
