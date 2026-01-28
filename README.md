# Tokenization Service

A Spring Boot 4.0.2 microservice that provides tokenization and detokenization APIs using Redis as the backing store.

## Features
- Tokenize account numbers into secure tokens
- Detokenize tokens back to original accounts
- Redis-backed storage
- Spring Boot 4 architecture
- Full test suite using MockMvc + Mockito

## Tech Stack
- Java 21
- Spring Boot 4.0.2
- Redis
- JUnit 5
- Mockito
- Embedded Redis (for tests)

## API Endpoints
### Tokenize
POST /api/v1/tokenization/tokenize [ "account1", "account2" ]

### Detokenize
POST /api/v1/tokenization/detokenize [ "token1", "token2" ]


