# 🏗️ High Level Design (HLD)

# Weather Intelligence

> **Version:** 1.0
> **Architecture:** Clean Architecture + MVVM
> **Platform:** Android
> **UI:** Jetpack Compose + Material 3

---

# Table of Contents

1. Overview
2. Goals
3. System Architecture
4. Component Diagram
5. Application Layers
6. Data Flow
7. User Journey
8. Network Flow
9. Offline Strategy
10. Background Sync
11. Cache Strategy
12. Navigation Flow
13. Security Overview
14. Performance Strategy
15. Scalability
16. Technology Stack
17. Risks & Mitigations

---

# 1. Overview

Weather Intelligence is a production-inspired Android application designed around the **Offline-First** principle.

The application provides weather information with minimal network dependency by storing weather data locally and refreshing it intelligently.

Key objectives:

* Fast startup
* Smooth UI
* Reliable offline experience
* Minimal API calls
* Battery-efficient synchronization
* Modular architecture

---

# 2. Goals

### Functional Goals

* Current Weather
* Hourly Forecast
* 7-Day Forecast
* Search City
* Favorite Cities
* Background Sync
* Offline Support

### Technical Goals

* Clean Architecture
* Reactive UI
* Easy Testing
* Scalability
* Maintainability

---

# 3. System Architecture

```text
                    ┌────────────────────────┐
                    │        User            │
                    └────────────┬───────────┘
                                 │
                                 ▼
                    ┌────────────────────────┐
                    │   Compose UI (M3)      │
                    └────────────┬───────────┘
                                 │
                                 ▼
                    ┌────────────────────────┐
                    │      ViewModel         │
                    └────────────┬───────────┘
                                 │
                                 ▼
                    ┌────────────────────────┐
                    │       UseCases         │
                    └────────────┬───────────┘
                                 │
                                 ▼
                    ┌────────────────────────┐
                    │     Repository         │
                    └──────┬─────────┬───────┘
                           │         │
                           ▼         ▼
                ┌──────────────┐  ┌──────────────┐
                │ Room Database│  │ Retrofit API │
                └──────────────┘  └──────────────┘
```

---

# 4. Component Diagram

```text
Application

├── Presentation
│     ├── Screens
│     ├── Components
│     ├── Navigation
│     └── ViewModels
│
├── Domain
│     ├── Models
│     ├── Repository Interfaces
│     └── UseCases
│
├── Data
│     ├── Local
│     ├── Remote
│     ├── Repository
│     └── Mapper
│
├── Worker
│
└── Core
```

---

# 5. Layer Responsibilities

## Presentation

Responsible for:

* Rendering UI
* User Interaction
* Navigation
* Collecting Flow
* State Rendering

---

## Domain

Responsible for:

* Business Logic
* Rules
* Use Cases
* Repository Contracts

---

## Data

Responsible for:

* Networking
* Room Database
* Repository Implementation
* Cache
* DTO Mapping

---

# 6. Data Flow

```text
User

↓

Compose

↓

ViewModel

↓

UseCase

↓

Repository

↓

Room

↓

Flow

↓

Compose

↑

Retrofit

↓

Save to Room

↓

Flow Emits

↓

UI Refresh
```

Only the Room database is observed by the UI.

---

# 7. User Journey

```text
Launch App

↓

Splash

↓

Read Cached Weather

↓

Display Cached Data

↓

Check TTL

↓

Expired?

├── No → Continue
│
└── Yes
      ↓
   Fetch API
      ↓
 Update Room
      ↓
UI Refresh
```

---

# 8. Network Flow

```text
API Request

↓

Retrofit

↓

DTO

↓

Mapper

↓

Entity

↓

Room

↓

Flow

↓

UI
```

The UI never consumes network DTOs directly.

---

# 9. Offline Strategy

Room Database acts as the **Single Source of Truth (SSOT).**

Benefits:

* Instant startup
* Offline usage
* Automatic updates
* Reduced API dependency

---

# 10. Background Sync

Implemented using WorkManager.

```text
Periodic Worker

↓

Network Available?

↓

Yes

↓

Check TTL

↓

Expired?

↓

Download

↓

Update Room

↓

Flow Emits

↓

Compose Refresh
```

---

# 11. Cache Strategy

| Data            | TTL        |
| --------------- | ---------- |
| Current Weather | 15 Minutes |
| Hourly Forecast | 1 Hour     |
| Daily Forecast  | 3 Hours    |
| AQI             | 1 Hour     |

Manual refresh bypasses TTL.

---

# 12. Navigation Flow

```text
Splash

↓

Home

├── Search

├── Weather Details

└── Settings
```

Navigation is implemented using **Navigation Compose**.

---

# 13. Security Overview

* API key stored outside source control (`local.properties`).
* HTTPS enforced.
* No sensitive user data stored.
* Room stores only weather/cache data.
* Hilt manages dependencies securely.

---

# 14. Performance Strategy

Performance goals:

* Cold Start < 2 sec
* Offline launch < 500 ms
* 60 FPS scrolling
* Minimal allocations
* Efficient recomposition
* Lazy lists for forecasts
* Background sync only when required

---

# 15. Scalability

The architecture supports future features with minimal changes:

* Weather widgets
* Air Quality
* Weather Maps
* Multiple API providers
* Wear OS
* Foldables
* Multi-module architecture

---

# 16. Technology Stack

| Layer         | Technology            |
| ------------- | --------------------- |
| Language      | Kotlin                |
| UI            | Jetpack Compose       |
| Design        | Material 3            |
| Architecture  | Clean Architecture    |
| Presentation  | MVVM                  |
| DI            | Hilt                  |
| Local Storage | Room                  |
| Networking    | Retrofit              |
| Async         | Coroutines            |
| Reactive      | Flow                  |
| Background    | WorkManager           |
| Preferences   | DataStore             |
| Image Loading | Coil                  |
| Logging       | Timber                |
| Testing       | JUnit, MockK, Turbine |

---

# 17. Risks & Mitigations

| Risk                 | Mitigation                    |
| -------------------- | ----------------------------- |
| API downtime         | Use cached Room data          |
| Network unavailable  | Offline-first strategy        |
| Battery optimization | WorkManager constraints       |
| API rate limiting    | Smart TTL caching             |
| Large datasets       | Pagination-ready architecture |

---

# High-Level Summary

The application is designed around the following principles:

* Clean Architecture
* MVVM
* Repository Pattern
* Offline-First
* Single Source of Truth
* Reactive UI with Flow
* Material 3 Design
* Battery-efficient synchronization
* Testability
* Scalability

These design decisions ensure the project is maintainable, production-ready, and suitable for future expansion without major architectural changes.
