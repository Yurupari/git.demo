# Git Demo

## Project Overview
This project is a Spring Boot application that provides an API to calculate a "popularity score" for Git repositories based on various parameters. It is designed to be run in a Docker container.

## Architecture
This project follows a monolithic architecture and is built using the following technologies:
- **Java 23**
- **Spring Boot 3.3.1**
- **Gradle 8.14.1**
- **Docker**
- **H2 Database** (for local development)
- **OpenFeign** (for declarative REST API communication)

## API Documentation
The application exposes the following endpoints:

### Health API Endpoint
`http://localhost:8080/actuator/health`

### Calculate Popularity Score
- **Endpoint**: `POST /api/git/v1/popularity/score`
- **Description**: Calculates a popularity score for Git repositories based on the provided filters.

#### Request Body
The request body should be a JSON object with the following structure:
```json
{
  "parameters": {
    "language": "java",
    "created_at": "2023-01-01",
    "stargazers_count": ">100"
  },
  "perPage": 10,
  "page": 1
}
```

The `parameters` object can contain the following keys, from which the API will search in the GitHub API:
- `language`
- `created_at`
- `updated_at`

The calculation of the popularity score is done using parameters set in the `application.yaml` file. For now only is done using the following parameters with its respective factor:
- `stargazers_count`: 1.5
- `forks_count`: 2.0
- `updated_at`: 50.0

For the first 2, it only checks the value of each item and multiply it by its respective weight, but for the last one, divide the weight by the number of days since updated plus 1.

#### Response Body
The response body will be a JSON object based on the structure of the [GitHub API response](https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28#search-repositories), with an additional `popularity_score` parameter:
```json
{
  "totalCount": 1,
  "incompleteResults": false,
  "items": [
    {
      ...,
      "popularity_score": 180
    }
  ]
}
```

### Error Response Body
The error response body will be a JSON object similar to this structure:
```json
{
    "httpStatus": "BAD_REQUEST",
    "timeStamp": "20251215 09:24:12",
    "message": "JSON parse error: Cannot deserialize Map key of..."
}
```

## Swagger UI / OpenAPI
The application provides an interactive API documentation through Swagger UI. Once the application is running, you can access it at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## Deployment
1. Download and install [Git](https://git-scm.com/install/).
2. Download and install [OpenJDK 23](https://adoptopenjdk.net/).
3. Download and install [Gradle](https://gradle.org/install/) using the version [8.14.1](https://gradle.org/releases/#8.14.1) to avoid possible problems in the building.
4. Download and install [Docker](https://docs.docker.com/engine/install/) and [Docker Compose](https://docs.docker.com/compose/install/).
5. Clone the following repository into your local machine using the following command:
   ```
   git clone https://github.com/Yurupari/git.demo.git
6. Enter the repository folder, and build the project using the following command:
   ```
   ./gradlew build
7. Run the application with Docker using the following command:
   ```
   docker-compose up -d
   ```
   * NOTE: If you're running on a local machine instead of a server, you can use a personalized configuration through the `docker-compose-local.yaml` file and execute the following command:
     ```
     docker-compose -f docker-compose-local.yaml up -d

## Testing
To run the tests and generate a coverage report, execute the following command:
```
./gradlew test --rerun-tasks
```
This command will:
- Run all unit and integration tests.
- Display a summary of test results directly in the console.
- Generate a detailed HTML test report at `build/reports/tests/test/index.html`.
- Generate a detailed HTML code coverage report at `build/reports/jacoco/html/index.html`.
