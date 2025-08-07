# Ticket Management System

A simple ticket management system with user and agent roles, implemented using **Java 21**, **Spring Boot**, **Docker**, and **Maven**.


## Prerequisites

Before you begin, ensure the following tools are installed:

- [Java 21](https://jdk.java.net/21/)
- [Docker](https://www.docker.com/)
- [Maven](https://maven.apache.org/) or use the included `mvnw` wrapper

## Setup and run

### Build

Use Maven Wrapper to build the project:

``` ./mvnw clean install ```

Or with Maven:

 ```mvn clean install ```

### Run
To start the application with its dependencies (e.g., database):

 ``` docker compose up ```

This will start the Spring Boot application and a PostgreSQL database

## Usage

### Login Agent
```
curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data '{
"username": "agent1",
"password": "agentpass"
}'
```
### Login USER
```
curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data '{
"username": "john",
"password": "pass123"
}'
```

### Response example
```
{
    "token": "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiZGlyIn0..VummURyL_E6L8-Sw.ipfTl6MaQ9p8L1JaIP8-eMQ2FSs9n1TVxN7k5OizhYMg9dxY3eamDFuSL0MUOtXBIOvtLScWgFsHaxxjuvkQ.tuQ19BzfbId8H4iF_xuRGQ"
}
```
### Create ticket example
For testing purposes agent id can be found printed in the console during application startup
```
curl --location 'http://localhost:8080/tickets' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiZGlyIn0..-VSk0uRg_TCtWLS3.GRQlEzgDFeB-2H-jORNmJ_RJ3h3fBm5hoxV2xYi3WDZZQsuElPeZC-sum71ltXwjq2KO33l1AbbWuOt0TnYx5Q.ny9Lo4jjuzbE401hrtTiwQ' \
--data '{
  "subject": "Still crashin 1",
  "description": "The app crashes when I click Submit too 1",
  "assigneeId": "{AGENT ID}"
}'
```

### List ticket example

```
curl --location 'http://localhost:8080/tickets' \
--header 'Authorization: Bearer eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiZGlyIn0..-VSk0uRg_TCtWLS3.GRQlEzgDFeB-2H-jORNmJ_RJ3h3fBm5hoxV2xYi3WDZZQsuElPeZC-sum71ltXwjq2KO33l1AbbWuOt0TnYx5Q.ny9Lo4jjuzbE401hrtTiwQ'
```
in the project folder you can also find the postman collection.

## How JWE is used

### Token Generation (Login)

When a user logs in via POST /auth/login:

The login controller calls AuthService.login(username, password).

On successful authentication, the method encrypts the user's ID and role into a JWE token using JweUtils.generateToken(userId, role) ,
returns this token in a TokenResponse.

Algorithm is direct symmetric encryption (dir) using AES (A256GCM) with 32 bytes long as a secret key.

### IMPORTANT
For simplicity and saving time purposes only secret key is hardcoded , in real prod environment we should take secret key from env variables or dedicated services such as AWS Secret Manager 
Additionally TTL management should be added to the security flow alongside with claims such as iat, nbf etc. 

### Token Verification (Every Request)

For every request to a protected endpoint (e.g., /tickets), custom JweAuthFilter does the following:

Extracts the Authorization header. If it contains a Bearer <token>, it attempts to decrypt the token with JweUtils.decrypt(token),
then extract userId and role. Build a CurrentUser object and set it as the authenticated principal in SecurityContext.

This enables Spring Security to authorize requests via @PreAuthorize annotations or role checks.

## AI Tools usage

1. ChatGPT for research and some configuration/boilerplate code generation(and for readme).
2. Github Copilot for code autocompletion

## Improvements

I would like to suggest some improvements that I did not do because of time limit of 90 minutes

1. Claims, TTL configuration, secret key securely storage
2. Integration tests with test containers and more unit tests coverage
3. DB init and migration with Liquibase
4. @Version and Auditability
5. Logging and metrics
6. Proper mapper(MapStruct)
7. More complex bean validation
8. Better exception handling with advices