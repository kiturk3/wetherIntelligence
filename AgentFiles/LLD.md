# 🏗️ Low Level Design (LLD)

# Weather Intelligence

**Version:** 1.0

**Platform:** Android

**Language:** Kotlin

**Architecture:** Clean Architecture + MVVM

---

# Table of Contents

1. Purpose
2. Package Structure
3. Module Responsibilities
4. Class Responsibilities
5. Domain Layer
6. Data Layer
7. Presentation Layer
8. Dependency Injection
9. Navigation
10. State Management
11. Database Design
12. Networking
13. Background Sync
14. Error Handling
15. Threading Strategy
16. Folder Structure
17. Naming Conventions

---

# 1. Purpose

This document describes the implementation-level design of the Weather Intelligence application.

It acts as the engineering blueprint used during development.

Goals:

* Maintainability
* Testability
* Scalability
* Readability

---

# 2. Package Structure

```text
com.krutik.weatherintelligence

├── app
├── core
├── data
├── domain
├── presentation
├── worker
├── di
└── MainActivity
```

---

# 3. Detailed Package Structure

```text
core

├── common
├── constants
├── dispatcher
├── extensions
├── navigation
├── designsystem
├── network
├── result
└── utils
```

---

```text
data

├── local
│     ├── dao
│     ├── entity
│     ├── database
│     └── converter
│
├── remote
│     ├── api
│     ├── dto
│     └── interceptor
│
├── mapper
│
├── repository
│
└── datasource
```

---

```text
domain

├── model
├── repository
├── usecase
└── validation
```

---

```text
presentation

├── home
├── search
├── details
├── settings
├── components
├── theme
└── navigation
```

---

# 4. Domain Layer

The Domain layer contains pure business logic.

No Android dependencies are allowed.

---

## Models

Weather

Forecast

HourlyForecast

City

WeatherAlert

FavoriteCity

Settings

---

## Repository Interface

```kotlin
interface WeatherRepository
```

Responsibilities:

* Get Current Weather
* Get Forecast
* Search City
* Save Favorites
* Refresh Weather
* Observe Weather

---

## UseCases

GetCurrentWeatherUseCase

GetForecastUseCase

SearchCityUseCase

RefreshWeatherUseCase

SaveFavoriteCityUseCase

DeleteFavoriteCityUseCase

ObserveWeatherUseCase

GetSettingsUseCase

UpdateSettingsUseCase

---

# 5. Data Layer

Responsible for

* Networking
* Database
* Cache
* Repository implementation

---

## Local

Room Database

### Database

WeatherDatabase

Contains

* WeatherDao
* ForecastDao
* FavoriteDao

---

## Entities

WeatherEntity

ForecastEntity

HourlyForecastEntity

FavoriteCityEntity

CacheMetadataEntity

---

## DAO

WeatherDao

Functions

Insert

Update

Delete

Observe

GetByCity

ClearExpired

---

ForecastDao

ObserveForecast

InsertForecast

DeleteForecast

---

FavoriteDao

InsertFavorite

DeleteFavorite

ObserveFavorites

---

# Remote Layer

Retrofit

---

API Interface

WeatherApi

Functions

Current Weather

Forecast

Search City

Air Quality (Future)

---

DTO

WeatherResponseDto

ForecastResponseDto

HourlyDto

CityDto

---

Mapper

WeatherMapper

ForecastMapper

HourlyMapper

---

Repository Implementation

WeatherRepositoryImpl

Responsibilities

* Check Cache

* Decide Refresh

* Fetch API

* Save Room

* Emit Flow

---

# 6. Presentation Layer

Presentation uses MVVM.

---

Home Screen

Components

HomeScreen

HomeViewModel

HomeUiState

HomeEvent

HomeAction

---

Search

SearchScreen

SearchViewModel

SearchUiState

---

Details

WeatherDetailsScreen

WeatherDetailsViewModel

---

Settings

SettingsScreen

SettingsViewModel

---

Reusable Components

WeatherCard

ForecastCard

HourlyCard

LoadingView

ErrorView

SearchBar

WeatherIcon

GradientBackground

SectionTitle

AnimatedTemperature

---

# 7. ViewModel Responsibilities

Each ViewModel

* Collect Flow
* Expose UiState
* Handle Events
* Call UseCases

Never

* Access Room

Never

* Call Retrofit

Never

* Execute SQL

---

# 8. UI State

Each screen exposes immutable state.

Example

HomeUiState

Loading

Success

Error

Empty

Refreshing

---

# 9. Navigation

Navigation Compose

Graph

```text
Splash

↓

Home

├── Search

├── Details

└── Settings
```

Arguments

City Name

Coordinates

Weather ID

---

# 10. Dependency Injection

Hilt Modules

NetworkModule

DatabaseModule

RepositoryModule

DispatcherModule

WorkerModule

PreferenceModule

---

Injected Objects

Retrofit

OkHttp

Room

DAO

Repository

UseCases

Worker

Dispatcher

DataStore

---

# 11. Database Design

Room

Tables

Weather

Forecast

HourlyForecast

FavoriteCity

CacheMetadata

Relationships

Weather

↓

Forecast

↓

HourlyForecast

CacheMetadata

stores

TTL

UpdatedAt

ExpiresAt

---

# 12. Networking

Retrofit

↓

OkHttp

↓

Logging Interceptor

↓

API

↓

DTO

↓

Mapper

↓

Entity

↓

Room

---

# 13. Background Worker

WeatherSyncWorker

Responsibilities

Check Internet

↓

Check TTL

↓

Fetch API

↓

Update Room

↓

Notify UI

Constraints

Network Connected

Battery Not Low

Retry Policy

Exponential Backoff

---

# 14. Error Handling

Repository converts exceptions into domain errors.

NetworkException

ApiException

CacheException

DatabaseException

UnknownException

UI never displays raw exceptions.

---

# 15. Threading Strategy

Main

Compose UI

IO

Room

Retrofit

WorkManager

Default

Heavy Computation

ViewModel uses

viewModelScope

Repository uses

DispatcherProvider

---

# 16. Folder Structure

```text
presentation

home

HomeScreen.kt

HomeViewModel.kt

HomeUiState.kt

HomeEvent.kt

HomeAction.kt

details

WeatherDetailsScreen.kt

WeatherDetailsViewModel.kt

search

SearchScreen.kt

SearchViewModel.kt

settings

SettingsScreen.kt

SettingsViewModel.kt
```

---

# 17. Naming Conventions

Classes

PascalCase

Functions

camelCase

Constants

UPPER_SNAKE_CASE

Composable

Suffix

Screen

Reusable UI

Suffix

Card

Repository

Suffix

Repository

UseCases

Suffix

UseCase

DTO

Suffix

Dto

Entity

Suffix

Entity

DAO

Suffix

Dao

ViewModel

Suffix

ViewModel

State

Suffix

UiState

---

# Class Interaction Overview

```text
Compose Screen

↓

ViewModel

↓

UseCase

↓

Repository Interface

↓

RepositoryImpl

↓

Room + Retrofit

↓

Flow

↓

Compose
```

---

# LLD Summary

The implementation follows:

* Clean Architecture
* MVVM
* Repository Pattern
* Offline-First
* Single Source of Truth
* Dependency Injection using Hilt
* Reactive state using Kotlin Flow
* Immutable UI State
* Material 3 Design System

Each class has a single responsibility, every dependency points inward, and business logic remains framework-independent, ensuring the application is scalable, maintainable, and production-ready.
