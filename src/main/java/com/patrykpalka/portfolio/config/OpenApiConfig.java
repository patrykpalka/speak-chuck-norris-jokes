package com.patrykpalka.portfolio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Chuck Norris Jokes API")
                        .version("1.0")
                        .description("This API provides Chuck Norris jokes with text-to-speech capabilities")
                        .contact(new Contact()
                                .name("Patryk Palka")
                                .email("exampleEmail@gmail.com")
                                .url("https://github.com/patrykpalka")));
    }
}
