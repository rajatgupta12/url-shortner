# URL Shortener

A URL Shortener service implemented with Java and Spring Boot.

This service accepts long URLs and returns short, easy-to-share identifiers. It uses a counter-based approach (Hi‑Lo style allocation) to generate compact, collision-free IDs at scale: a high/low sequence lets the application allocate ranges of numeric IDs locally while the central counter in the database provides the high component. This reduces round-trips and improves throughput compared to requesting a new counter value for every short URL.

<b>Key features</b>
- Short URL creation and permanent redirects (301).
- Counter-based ID generation (Hi‑Lo allocation) for short codes.
- Authentication & authorization for admin operations.
- Authenticate the user before accessing the URL creation endpoint.
- Rate limiting on creation endpoints to avoid abuse (Resilience4j).

<b>Tech stack</b>
- Java 21
- Spring Boot (Web, Security)
- MongoDB (as the primary datastore)
- MySQL (as User authentication data)
- Gradle build system
- Resilience4j (rate limiting)
- OpenAPI / Swagger for API docs

<b>Project structure</b>
- `src/main/java/com/example/urlshortner/`
  - `controller/` - REST controllers (e.g. `UrlMappingController`, `AuthController`, `AdminController`)
  - `service/` - business logic and Hi‑Lo id allocation service
  - `repository/` - MySQL, MongoDB repositories
  - `model/` and `dto/` - domain models and transfer objects
  - `security/` - authentication and authorization pieces
  - `aspect/` - AOP utilities (logging, metrics)
  - `config/` - OpenAPI, application configuration
- `src/main/resources/application.properties` - application configuration (Mongo, MySQL, rate limiter, security)

<b>Authentication and authorization</b>
- The application exposes protected endpoints which require authentication. Typical setup used here is token-based (for example JWT) authentication: users call an auth endpoint to receive a token, then present that token in the `Authorization: Bearer <token>` header for protected requests.
- Role-based authorization is applied where appropriate. For example, endpoints under admin controllers require an `ADMIN` role while create/read operations for users require an authenticated user.
- (Assumption: the repo contains Spring Security + a token authentication flow. If you use a different method, adjust the config accordingly.)

<b>Rate limiter</b>
- Rate limiting is implemented using Resilience4j's RateLimiter module.
- The create short-URL endpoint is protected by a rate limiter to prevent abuse. You can configure limits (requests per period, timeout) in `application.properties` or Spring config.
- The code supports annotation-based declarations (e.g. `@RateLimiter(name = "createUrlRateLimiter")`) as well as programmatic registration.

<b>Backend setup</b>
1. Ensure you have Java 21, Gradle, and a running MySQL, MongoDB instance.
2. Copy/modify `src/main/resources/application.properties` to provide MongoDB connection details. Example minimal properties:

spring.data.mongodb.uri=mongodb://localhost:27017
spring.data.mongodb.database=url_shortner

3. Build and run with Gradle (wrapper):

./gradlew clean build
./gradlew bootRun

4. Open the API docs at: http://localhost:8080/swagger-ui.html or the OpenAPI path configured in the app.

<b>Docker</b>

- A Dockerfile is included. Build the application JAR and image:

```bash
./gradlew bootJar
docker build -t url-shortner:latest .
```

- Run the container (example exposing port 8080 and providing MongoDB details via env):

```bash
docker run -p 8080:8080 url-shortner:latest
```

<b>Endpoints</b>

1) Create short URL
- Endpoint: POST /url
- Description: Creates a short URL mapping for a provided long URL. This endpoint is typically rate-limited.
- Request (JSON):
  {
    "url": "https://example.com/some/very/long/path"
  }
- Response (201 Created):
  {
    "shortUrl": "http://your-domain/1",
    "longUrl": "https://example.com/some/very/long/path"
  }
- Error responses:
  - 400 Bad Request: invalid payload or URL
  - 409 Conflict: custom alias already taken
  - 429 Too Many Requests: rate limit exceeded

Examples (curl):

curl -X POST http://localhost:8080/url \
  -H "Content-Type: application/json" \
  -d '{"url":"https://example.com/"}'

2) Redirect by short id
- Endpoint: GET /{id}
- Description: Looks up the original URL for the short id and redirects the client to it with HTTP 301 (Moved Permanently). Also increments a hit counter for analytics.
- Response:
  - 301 Moved Permanently with header `Location: https://example.com/your/long/path`
  - If not found -> 404 Not Found

Example (curl):

curl -i http://localhost:8080/1

Interpreting the redirect response (example raw headers):
HTTP/1.1 301 Moved Permanently
Location: https://example.com/some/very/long/path
Content-Length: 0