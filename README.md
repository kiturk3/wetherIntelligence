# 🌦️ Weather Intelligence

> **A modern, offline-first Android Weather Application built using Clean Architecture, Jetpack Compose, Material 3, Hilt, Room, Coroutines, Flow, and WorkManager.**

![Android](https://img.shields.io/badge/Android-24%2B-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-2.x-blue)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-Latest-success)
![Material3](https://img.shields.io/badge/Material%203-Expressive-purple)
![Architecture](https://img.shields.io/badge/Architecture-Clean-orange)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

# 📖 Overview

Weather Intelligence is a production-inspired Android application that provides real-time weather information while following an **Offline-First** architecture.

Instead of directly displaying data from the network, the application uses **Room Database as the Single Source of Truth (SSOT)**, ensuring the app remains responsive even when internet connectivity is unavailable.

The project demonstrates modern Android development practices and focuses on:

* Clean Architecture
* SOLID Principles
* Offline-first design
* Reactive programming with Kotlin Flow
* Material 3 UI
* Background synchronization
* Smart caching using TTL
* Testability
* Scalability

---

# ✨ Features

## Current Weather

* Current Temperature
* Weather Condition
* Feels Like
* Wind Speed
* Humidity
* Pressure
* Visibility
* UV Index
* Sunrise & Sunset

---

## Forecast

* 24 Hour Forecast
* 7 Day Forecast
* Hourly Temperature
* Weather Icons

---

## Offline First

* Works without Internet
* Cached Weather
* Room Database
* Automatic Synchronization
* Smart Refresh Strategy

---

## Smart Cache

Instead of calling the API every launch:

* Current Weather → 15 Minutes TTL
* Forecast → 3 Hours TTL
* AQI → 1 Hour TTL

This reduces API calls while improving performance.

---

## Background Sync

* WorkManager
* Battery Optimized
* Network Constraints
* Automatic Updates

---

## Dynamic Material 3 UI

* Dynamic Colors
* Dark Theme
* Light Theme
* Weather-based Gradients
* Smooth Animations
* Edge-to-Edge Layout

---

## Search

* Search City
* Recent Searches
* Favorite Cities

---

# 📱 Screens

| Screen   | Description                  |
| -------- | ---------------------------- |
| Splash   | App initialization           |
| Home     | Current weather dashboard    |
| Search   | Search weather by city       |
| Details  | Extended weather information |
| Settings | Units, theme, notifications  |

---

# 🏗 Architecture

The project follows **Clean Architecture** with clear separation of concerns.

```text
                    UI
                     │
             Presentation
             (ViewModel)
                     │
                Use Cases
                     │
              Repository
              /         \
      Local DB      Remote API
        (Room)      (Retrofit)
              \         /
          Single Source of Truth
```

---

# 📂 Project Structure

```text
Weather-Intelligence/

├── app/
├── core/
│   ├── common/
│   ├── designsystem/
│   ├── navigation/
│   └── utils/
│
├── data/
│   ├── local/
│   ├── remote/
│   ├── mapper/
│   └── repository/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── presentation/
│   ├── home/
│   ├── details/
│   ├── search/
│   ├── settings/
│   └── components/
│
├── worker/
├── di/
└── docs/
```

---

# 🛠 Tech Stack

| Category             | Technology            |
| -------------------- | --------------------- |
| Language             | Kotlin                |
| Architecture         | Clean Architecture    |
| UI                   | Jetpack Compose       |
| Design               | Material 3            |
| Dependency Injection | Hilt                  |
| Local Storage        | Room                  |
| Networking           | Retrofit              |
| JSON                 | Kotlinx Serialization |
| Concurrency          | Kotlin Coroutines     |
| Reactive             | Kotlin Flow           |
| Background Tasks     | WorkManager           |
| Image Loading        | Coil                  |
| Preferences          | DataStore             |
| Logging              | Timber                |
| Testing              | JUnit, MockK, Turbine |

---

# 🔄 Data Flow

```text
User

↓

ViewModel

↓

UseCase

↓

Repository

↓

Room Database

↓

UI Updates

↑

Retrofit

↓

Room Update

↓

Flow Emits

↓

Compose Recomposition
```

---

# 🌐 Offline First Strategy

The application uses **Room Database as the Single Source of Truth.**

```text
App Launch

↓

Read Local Database

↓

Data Available?

↓

YES

↓

Display Immediately

↓

Check Cache TTL

↓

Expired?

↓

YES

↓

Call API

↓

Save Room

↓

UI Updates Automatically
```

---

# 🔐 Smart Refresh Strategy

| Data            | Refresh Interval |
| --------------- | ---------------- |
| Current Weather | 15 Minutes       |
| Hourly Forecast | 1 Hour           |
| Daily Forecast  | 3 Hours          |
| Air Quality     | 1 Hour           |

---

# 🧩 Design Principles

* SOLID
* DRY
* KISS
* Single Source of Truth
* Repository Pattern
* Dependency Injection
* State Hoisting
* Immutable UI State

---

# 🚀 Getting Started

## Prerequisites

* Android Studio Narwhal or newer
* JDK 17
* Android SDK 24+
* Kotlin 2.x

---

## Clone Repository

```bash
git clone https://github.com/<your-username>/weather-intelligence.git
```

---

## Open Project

Open with Android Studio.

---

## Add API Key

Create a `local.properties` file.

```properties
WEATHER_API_KEY=YOUR_API_KEY
```

---

## Build

```bash
./gradlew assembleDebug
```

---

## Run

```bash
./gradlew installDebug
```

---

# 🧪 Testing

Run all unit tests.

```bash
./gradlew test
```

Run instrumentation tests.

```bash
./gradlew connectedAndroidTest
```

---

# 📈 Performance Goals

* Cold Start < 2 seconds
* Offline Launch < 500 ms
* 60 FPS Scrolling
* Battery Efficient Sync
* Minimal Network Usage

---

# ♿ Accessibility

* Material 3 Accessibility
* Dynamic Font Scaling
* High Contrast Support
* TalkBack Compatible
* Minimum Touch Target 48dp

---

# 🔮 Future Enhancements

* Weather Maps
* Air Quality Dashboard
* Widgets
* Wear OS
* Foldable Support
* AI Weather Insights
* Multiple Weather Providers
* Cloud Backup

---

# 📸 Screenshots

> Screenshots will be added after implementation.

```
docs/screenshots/

├── splash.png
├── home.png
├── search.png
├── details.png
├── settings.png
```

---

# 📚 Documentation

| Document         | Description                   |
| ---------------- | ----------------------------- |
| PRD.md           | Product Requirements Document |
| ARCHITECTURE.md  | Clean Architecture            |
| HLD.md           | High Level Design             |
| LLD.md           | Low Level Design              |
| API.md           | API Documentation             |
| DATABASE.md      | Room Database Design          |
| OFFLINE_FIRST.md | Caching & Sync Strategy       |
| UI_UX.md         | Material 3 Design System      |
| TESTING.md       | Testing Strategy              |

---

# 🤝 Contributing

Contributions are welcome.

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push the branch
5. Open a Pull Request

---

# 📄 License

This project is licensed under the MIT License.

---

# 👨‍💻 Author

**Krutik Khokhara**

Senior Android Engineer

---

> Built with ❤️ using Kotlin, Jetpack Compose, Material 3, Clean Architecture, and modern Android development best practices.
