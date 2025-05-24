# Speak Chuck Norris Jokes

Spring Boot application providing an API to fetch random Chuck Norris jokes and play them using text-to-speech (Voice RSS).

## Features

- Fetch a random Chuck Norris joke (optionally by category)
- Play the joke as audio (Text-to-Speech)
- Retrieve a list of available joke categories
- OpenAPI/Swagger documentation

## Requirements

- Java 17+
- Maven

## Getting Started

1. Clone the repository:
```plaintext
git clone https://github.com/patrykpalka/speak-chuck-norris-jokes.git
```
2. Navigate to the project directory:
```plaintext
cd speak-chuck-norris-jokes
```
3. Build the project:
```plaintext
mvn clean install
```
4. Run the application:
```plaintext
mvn spring-boot:run
```

## API

After starting the application, the API documentation is available at:
`http://localhost:8080/swagger-ui.html`

### Example Endpoints

- `GET /jokes/random`  
  Returns a random joke and plays it as audio.  
  Optional parameter: `category`

- `GET /jokes/categories`  
  Returns a list of available joke categories.

## Technologies

- Spring Boot
- Spring WebFlux (WebClient)
- Springdoc OpenAPI (Swagger UI)
- Voice RSS API (Text-to-Speech)
- Java Sound API

## Author
[Patryk Palka - GitHub](https://github.com/patrykpalka)