# 📏 Coding Standards & Development Guidelines

# Weather Intelligence

**Version:** 1.0

**Language:** Kotlin

**Architecture:** Clean Architecture

**UI:** Jetpack Compose

---

# Table of Contents

1. Purpose
2. General Principles
3. Kotlin Coding Standards
4. Package Structure
5. Naming Conventions
6. Clean Architecture Rules
7. Compose Guidelines
8. ViewModel Guidelines
9. Repository Guidelines
10. Room Guidelines
11. Retrofit Guidelines
12. Coroutines & Flow
13. Dependency Injection
14. Error Handling
15. Logging
16. Performance Guidelines
17. Testing Standards
18. Git & Commit Standards
19. Code Review Checklist
20. Do's & Don'ts

---

# 1. Purpose

This document defines the coding standards followed throughout the Weather Intelligence project.

Objectives:

* Maintain consistency
* Improve readability
* Reduce bugs
* Simplify reviews
* Enable scalability
* Improve onboarding

---

# 2. General Principles

Every piece of code should follow:

* SOLID Principles
* DRY (Don't Repeat Yourself)
* KISS (Keep It Simple)
* YAGNI (You Aren't Gonna Need It)
* Single Responsibility Principle
* Composition over Inheritance
* Immutable State whenever possible

---

# 3. Kotlin Coding Standards

## Use Kotlin Features

Prefer:

* Data classes
* Sealed classes/interfaces
* Extension functions
* Inline functions (where appropriate)
* Object declarations for stateless utilities
* Null safety instead of defensive null checks

Avoid:

* Java-style getters/setters
* Static utility classes
* Platform types when possible
* Unnecessary mutable collections

Example:

```kotlin
val cities: List<City>

val uiState: StateFlow<HomeUiState>
```

Prefer immutable collections and `val` by default.

---

# 4. Package Structure

```text
com.krutik.weatherintelligence

core/
data/
domain/
presentation/
worker/
di/
```

Feature-specific code should stay within its feature package.

Avoid creating large "utils" packages that become dumping grounds.

---

# 5. Naming Conventions

## Classes

PascalCase

Examples:

HomeViewModel

WeatherRepository

ForecastMapper

---

## Functions

camelCase

Examples:

loadWeather()

refreshWeather()

searchCity()

---

## Variables

camelCase

Good:

temperature

weatherState

forecastItems

Avoid:

temp1

abc

data2

---

## Constants

UPPER_SNAKE_CASE

Example:

```kotlin
const val CACHE_DURATION_MINUTES = 15
```

---

## Files

Match the primary class name.

Example:

```text
HomeScreen.kt

HomeViewModel.kt

WeatherRepository.kt
```

---

# 6. Clean Architecture Rules

Presentation

❌ Must NOT know Retrofit

❌ Must NOT know Room

❌ Must NOT know DTO

Presentation only communicates with:

* ViewModel
* UiState

---

Domain

Must NOT depend on Android.

Allowed:

* Kotlin
* Coroutines

Not Allowed:

* Retrofit
* Room
* Context
* Compose

---

Data

Responsible for:

* API
* Database
* Cache
* Repository implementation

---

# 7. Compose Guidelines

Composable functions should:

* Be stateless whenever possible
* Receive state through parameters
* Emit events via callbacks
* Avoid business logic

Good:

```kotlin
WeatherCard(
    weather = state.weather,
    onRefresh = viewModel::refresh
)
```

Avoid:

```kotlin
WeatherCard() {
    repository.fetchWeather()
}
```

---

## State Hoisting

Always hoist state to the lowest common parent.

UI should display state, not own business logic.

---

## Recomposition

Avoid unnecessary recompositions.

Use:

* remember
* derivedStateOf
* rememberUpdatedState
* immutable UI models
* stable collections

---

# 8. ViewModel Guidelines

Responsibilities:

* Handle UI events
* Execute UseCases
* Expose StateFlow
* Transform domain models into UI state

Never:

* Access DAO directly
* Call Retrofit
* Use Android Views
* Hold Context (unless via Application when absolutely required)

---

# 9. Repository Guidelines

Repository responsibilities:

* Decide cache vs network
* Map DTOs to domain models
* Persist entities
* Expose Flow

Repository should never expose DTOs to higher layers.

---

# 10. Room Guidelines

Use:

* @Transaction for multi-table updates
* Indices on frequently queried columns
* Explicit migrations
* Flow-returning DAO methods

Avoid:

* Main-thread database access
* Raw SQL where Room annotations are sufficient

---

# 11. Retrofit Guidelines

Recommended configuration:

* Single Retrofit instance
* OkHttp interceptors
* HTTPS only
* API key injected via BuildConfig
* Kotlinx Serialization

Never expose Retrofit response models outside the Data layer.

---

# 12. Coroutines & Flow

Dispatchers:

* Main → UI
* IO → Database & Network
* Default → CPU-intensive work

Guidelines:

* Use `viewModelScope`
* Expose immutable `StateFlow`
* Avoid `GlobalScope`
* Cancel work with lifecycle
* Prefer `Flow` over callbacks

---

# 13. Dependency Injection

Use Hilt.

Inject:

* Repository
* UseCases
* Database
* DAO
* Retrofit
* Dispatchers

Avoid service locators or manual singleton patterns.

---

# 14. Error Handling

Convert low-level exceptions into domain-friendly errors.

Do not expose:

* IOException
* HttpException
* SQLiteException

Instead expose:

* NetworkError
* CacheError
* UnknownError

UI should render user-friendly messages.

---

# 15. Logging

Use Timber.

Development:

* Verbose logs
* Network logs
* Debug information

Release:

* Disable verbose logging
* Never log API keys
* Never log sensitive data

---

# 16. Performance Guidelines

Aim for:

* Cold start < 2 seconds
* Offline launch < 500 ms
* Smooth 60 FPS scrolling

Practices:

* LazyColumn for long lists
* Stable UI models
* Minimize allocations
* Use `remember` appropriately
* Batch Room writes in transactions

---

# 17. Testing Standards

Every new feature should include tests.

Minimum expectations:

* ViewModel tests
* UseCase tests
* Repository tests
* DAO tests (where applicable)

Critical user flows should have Compose UI tests.

---

# 18. Git & Commit Standards

Branch naming:

```text
feature/weather-search

feature/offline-cache

bugfix/api-timeout

refactor/home-screen
```

Commit messages (Conventional Commits):

```text
feat: add weather search

fix: prevent duplicate API calls

refactor: simplify repository cache logic

test: add HomeViewModel tests

docs: update architecture documentation
```

---

# 19. Code Review Checklist

Before merging:

* [ ] Code builds successfully
* [ ] Unit tests pass
* [ ] No lint errors
* [ ] No TODOs left unintentionally
* [ ] Public APIs documented
* [ ] No business logic in Composables
* [ ] No direct Retrofit usage in ViewModels
* [ ] No blocking calls on Main thread
* [ ] Error states handled
* [ ] Strings extracted to resources
* [ ] Accessibility reviewed

---

# 20. Do's & Don'ts

## ✅ Do

* Keep classes focused
* Write meaningful names
* Prefer immutability
* Use dependency injection
* Test business logic
* Keep UI declarative
* Handle loading and error states
* Document complex decisions

## ❌ Don't

* Put business logic inside Composables
* Access Room directly from ViewModels
* Expose DTOs outside the Data layer
* Use GlobalScope
* Hardcode API keys
* Ignore exceptions
* Create God classes
* Duplicate logic across features

---

# Coding Standards Summary

The Weather Intelligence project follows modern Android engineering practices by emphasizing:

* Clean Architecture
* SOLID principles
* Kotlin-first development
* Declarative UI with Jetpack Compose
* Offline-first design
* Reactive programming with Coroutines and Flow
* Dependency Injection with Hilt
* Comprehensive testing
* Consistent naming and project structure

Adhering to these standards ensures the codebase remains maintainable, scalable, readable, and suitable for long-term production development.
