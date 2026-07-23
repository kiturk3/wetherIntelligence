# 🗄️ Database Design Document

# Weather Intelligence

**Version:** 1.0

**Database:** Room

**Storage:** SQLite

**Architecture:** Offline-First

---

# Table of Contents

1. Overview
2. Design Goals
3. Database Architecture
4. Tables
5. Entity Relationships
6. Primary Keys
7. Index Strategy
8. Cache Strategy
9. DAO Design
10. Repository Interaction
11. Migrations
12. Performance Optimizations
13. Future Enhancements

---

# 1. Overview

Weather Intelligence follows an **Offline-First** architecture.

The local Room database acts as the **Single Source of Truth (SSOT)**.

The application never displays API responses directly.

Instead:

```text
API

↓

DTO

↓

Mapper

↓

Room Database

↓

Flow

↓

Compose UI
```

This ensures:

* Instant app startup
* Offline support
* Automatic UI updates
* Better user experience
* Lower API usage

---

# 2. Design Goals

The database is designed to:

* Work completely offline
* Store weather cache
* Reduce API calls
* Support multiple cities
* Enable future scalability
* Support WorkManager synchronization
* Maintain historical metadata

---

# 3. Database Architecture

```text
WeatherDatabase

├── WeatherDao
├── ForecastDao
├── FavoriteCityDao
├── CacheMetadataDao
└── SearchHistoryDao
```

Each DAO has a single responsibility.

---

# 4. Tables

## Weather

Stores current weather.

Columns

| Column             | Type   |
| ------------------ | ------ |
| cityId             | Long   |
| cityName           | String |
| latitude           | Double |
| longitude          | Double |
| temperature        | Double |
| feelsLike          | Double |
| humidity           | Int    |
| pressure           | Int    |
| visibility         | Int    |
| windSpeed          | Double |
| weatherCode        | Int    |
| weatherDescription | String |
| sunrise            | Long   |
| sunset             | Long   |
| updatedAt          | Long   |

---

## Forecast

Stores 7-day forecast.

Columns

| Column      | Type   |
| ----------- | ------ |
| forecastId  | Long   |
| cityId      | Long   |
| date        | Long   |
| minTemp     | Double |
| maxTemp     | Double |
| humidity    | Int    |
| windSpeed   | Double |
| icon        | String |
| description | String |

---

## HourlyForecast

Stores hourly weather.

Columns

| Column          | Type   |
| --------------- | ------ |
| hourlyId        | Long   |
| cityId          | Long   |
| timestamp       | Long   |
| temperature     | Double |
| icon            | String |
| rainProbability | Double |

---

## FavoriteCity

Stores favorite cities.

Columns

| Column    | Type   |
| --------- | ------ |
| cityId    | Long   |
| cityName  | String |
| latitude  | Double |
| longitude | Double |

---

## SearchHistory

Stores recently searched cities.

Columns

| Column     | Type   |
| ---------- | ------ |
| id         | Long   |
| cityName   | String |
| searchedAt | Long   |

---

## CacheMetadata

Stores cache information.

Columns

| Column      | Type   |
| ----------- | ------ |
| cacheKey    | String |
| lastUpdated | Long   |
| expiresAt   | Long   |

This table enables Smart TTL refresh.

---

# 5. Entity Relationship Diagram

```text
City
 │
 │ 1
 │
 ▼
Weather
 │
 ├─────────────┐
 │             │
 ▼             ▼
Forecast   HourlyForecast

City
 │
 ▼
FavoriteCity

City
 │
 ▼
SearchHistory

CacheMetadata
```

---

# 6. Primary Keys

| Table          | Primary Key |
| -------------- | ----------- |
| Weather        | cityId      |
| Forecast       | forecastId  |
| HourlyForecast | hourlyId    |
| FavoriteCity   | cityId      |
| SearchHistory  | id          |
| CacheMetadata  | cacheKey    |

---

# 7. Index Strategy

Indexes improve query performance.

Recommended indexes:

Weather

* cityId

Forecast

* cityId
* date

HourlyForecast

* cityId
* timestamp

SearchHistory

* searchedAt

CacheMetadata

* cacheKey

---

# 8. Cache Strategy

Instead of refreshing every launch:

| Data            | TTL        |
| --------------- | ---------- |
| Current Weather | 15 Minutes |
| Hourly Forecast | 1 Hour     |
| Daily Forecast  | 3 Hours    |
| Air Quality     | 1 Hour     |

The repository checks `CacheMetadata` before making a network request.

---

# 9. DAO Design

## WeatherDao

Responsibilities

* Insert weather
* Update weather
* Delete weather
* Observe weather
* Fetch by city

---

## ForecastDao

Responsibilities

* Insert forecast
* Observe forecast
* Delete forecast

---

## FavoriteCityDao

Responsibilities

* Add favorite
* Remove favorite
* Observe favorites

---

## SearchHistoryDao

Responsibilities

* Save search
* Observe history
* Delete old history

---

## CacheMetadataDao

Responsibilities

* Save TTL
* Read TTL
* Clear expired cache

---

# 10. Repository Interaction

```text
ViewModel

↓

UseCase

↓

Repository

↓

Read Room

↓

Cache Valid?

↓

YES

↓

Return Flow

NO

↓

API

↓

Mapper

↓

Save Room

↓

Flow Emits

↓

Compose Updates
```

Room remains the only observable data source.

---

# 11. Migration Strategy

Database versioning will use Room Migrations.

Example future changes:

Version 1

Current Weather

↓

Version 2

Add AQI

↓

Version 3

Add Weather Alerts

↓

Version 4

Add Weather Widgets

No destructive migrations should be used in production.

---

# 12. Performance Optimizations

* Use Flow for reactive updates
* Observe only required tables
* Add indexes to frequently queried columns
* Batch inserts using transactions
* Avoid unnecessary joins
* Delete expired cache periodically
* Lazy-load forecast data

---

# 13. Future Enhancements

Potential additions:

* Air Quality table
* Weather Alerts
* Radar Metadata
* User Preferences
* Widget Configuration
* Weather History
* Multiple Weather Providers
* Sync Queue
* Analytics Cache

---

# Database Summary

The Room database is designed to be:

* Offline-first
* Scalable
* Battery-efficient
* Testable
* Easy to migrate
* Reactive using Flow
* Optimized for weather data

By using Room as the Single Source of Truth and introducing a dedicated `CacheMetadata` table, the application minimizes unnecessary API requests while ensuring users always receive the freshest available data.
