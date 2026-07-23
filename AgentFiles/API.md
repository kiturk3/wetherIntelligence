# 🌐 API Design & Networking Documentation

# Weather Intelligence

**Version:** 1.0

**Networking:** Retrofit + OkHttp

**Serialization:** Kotlinx Serialization

**Architecture:** Clean Architecture

---

# Table of Contents

1. Overview
2. API Selection
3. Networking Architecture
4. Endpoint Design
5. Request Flow
6. DTO Design
7. Mapper Strategy
8. Repository Integration
9. Error Handling
10. Retry Strategy
11. Timeout Configuration
12. Interceptors
13. Authentication
14. Rate Limiting
15. Offline Strategy
16. Response Caching
17. Logging
18. Security
19. Future Improvements

---

# 1. Overview

The networking layer is responsible for retrieving weather information from the remote provider while remaining completely independent of the Presentation layer.

The application follows the Repository Pattern.

The UI never communicates directly with Retrofit.

```text
Compose UI

↓

ViewModel

↓

UseCase

↓

Repository

↓

RemoteDataSource

↓

Retrofit

↓

Weather API
```

---

# 2. API Selection

The assignment allows using any public weather API.

Primary choice:

**OpenWeather One Call API**

Reasons:

* Rich weather information
* Hourly forecast
* Daily forecast
* Current weather
* Stable documentation
* Large developer community

Alternative providers:

* WeatherAPI
* Open-Meteo
* Visual Crossing

The networking layer is provider-agnostic, allowing easy replacement in the future.

---

# 3. Networking Architecture

```text
Presentation

↓

Domain

↓

Repository

↓

RemoteDataSource

↓

Retrofit

↓

OkHttp

↓

Internet
```

Responsibilities:

## Retrofit

* API interface
* Request creation
* Response parsing

## OkHttp

* HTTP client
* Logging
* Timeouts
* Retry
* Interceptors

---

# 4. Endpoint Design

## Current Weather

Returns:

* Temperature
* Feels Like
* Humidity
* Pressure
* Wind
* Visibility
* Sunrise
* Sunset

---

## Hourly Forecast

Returns:

* Next 24 hours

Information:

* Temperature
* Weather icon
* Rain probability
* Wind

---

## Daily Forecast

Returns:

Next 7 days

Information:

* Min temperature
* Max temperature
* Condition
* Sunrise
* Sunset

---

## City Search

Returns:

* City Name
* Latitude
* Longitude
* Country

---

# 5. Request Flow

```text
User

↓

Search City

↓

ViewModel

↓

SearchCityUseCase

↓

Repository

↓

RemoteDataSource

↓

API

↓

DTO

↓

Mapper

↓

Domain Model

↓

Room

↓

Flow

↓

Compose UI
```

The UI observes Room only.

---

# 6. DTO Design

Remote models remain isolated.

Examples:

WeatherResponseDto

ForecastDto

HourlyDto

CityDto

AlertDto

DTOs are never exposed outside the Data layer.

---

# 7. Mapper Strategy

Every DTO is converted into:

Domain Model

↓

Room Entity

Benefits:

* API changes do not affect UI.
* Database schema remains independent.
* Easier testing.

```text
DTO

↓

Mapper

↓

Domain Model

↓

Entity
```

---

# 8. Repository Integration

Repository decides:

* Use Cache?
* Refresh?
* Return Local?
* Download?

```text
Repository

↓

Cache Valid?

YES

↓

Room

NO

↓

API

↓

Room

↓

Flow
```

The Presentation layer remains unaware of networking.

---

# 9. Error Handling

Network errors are transformed into domain errors.

Categories:

## No Internet

Display cached data.

---

## Timeout

Retry option.

---

## API Error

Show friendly message.

---

## Unknown Error

Fallback UI.

---

The UI never receives Retrofit exceptions directly.

---

# 10. Retry Strategy

Automatic retry is applied only for transient failures.

Examples:

* Timeout
* Temporary network loss
* HTTP 502
* HTTP 503

Retry Policy:

* Maximum 3 retries
* Exponential backoff

Do not retry:

* HTTP 400
* HTTP 401
* HTTP 403
* HTTP 404

---

# 11. Timeout Configuration

Recommended:

Connection Timeout

10 seconds

Read Timeout

15 seconds

Write Timeout

15 seconds

These values provide a balance between responsiveness and reliability.

---

# 12. Interceptors

## LoggingInterceptor

Development only.

Logs:

* URL
* Method
* Headers
* Response code
* Duration

Disabled in release builds.

---

## NetworkInterceptor

Responsibilities:

* Add common headers
* Compression
* Response validation

---

## RetryInterceptor (Optional)

Handles retry policy.

Responsibilities:

* Retry transient failures
* Exponential backoff

---

# 13. Authentication

API key is stored securely.

Never hardcode:

```text
❌ BuildConfig.API_KEY
❌ Source Code
❌ Git Repository
```

Recommended:

```text
local.properties

↓

BuildConfig

↓

Retrofit
```

API keys must be excluded from version control.

---

# 14. Rate Limiting

Avoid unnecessary API calls.

Strategy:

Current Weather

15 min TTL

Forecast

3 hr TTL

Manual Refresh

Always allowed

This dramatically reduces API usage.

---

# 15. Offline Strategy

Room remains the Single Source of Truth.

```text
API

↓

Mapper

↓

Room

↓

Flow

↓

UI
```

Never

```text
API

↓

UI
```

---

# 16. Response Caching

Caching occurs in Room rather than relying solely on HTTP cache.

Advantages:

* Offline support
* Fine-grained TTL
* Queryable data
* Persistent storage

---

# 17. Logging Strategy

Development:

Verbose logging

Production:

No body logging

Sensitive values hidden

Example logs:

Request URL

Response Time

HTTP Status

Failure Reason

API Duration

---

# 18. Security

Best Practices:

* HTTPS only
* API key outside repository
* Certificate validation
* Disable debug logging
* Avoid exposing raw server errors
* Sanitize exception messages

No sensitive user information is stored.

---

# 19. Future Improvements

Potential enhancements:

* Multiple weather providers
* Automatic provider failover
* GraphQL support
* HTTP cache layer
* ETag support
* Compression
* Network Quality Detection
* Circuit Breaker Pattern
* Request Queue
* Background Prefetching

---

# Recommended Package Structure

```text
data

remote

├── api
│      WeatherApi.kt
│
├── dto
│      WeatherDto.kt
│      ForecastDto.kt
│
├── mapper
│      WeatherMapper.kt
│
├── datasource
│      RemoteDataSource.kt
│
├── interceptor
│      LoggingInterceptor.kt
│      RetryInterceptor.kt
│
└── network
       NetworkMonitor.kt
```

---

# Networking Summary

The networking layer follows modern Android best practices by combining:

* Retrofit
* OkHttp
* Repository Pattern
* Clean Architecture
* DTO Mapping
* Kotlin Coroutines
* Room Caching
* Offline-First Design
* Secure API Key Management
* Structured Error Handling

This design ensures the application remains scalable, testable, resilient to network failures, and easy to extend with additional weather providers in the future.
