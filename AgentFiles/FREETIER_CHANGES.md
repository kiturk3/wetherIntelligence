# OpenWeather API Migration Guide
**Project:** Weather Intelligence Android App  
**Version:** 1.0  
**Author:** Krutik Khokhara  
**Last Updated:** July 2026

---

# Overview

This document describes the migration from **OpenWeather One Call 3.0 API** to the **free OpenWeather APIs**.

The application originally used:

```
GET /data/3.0/onecall
```

This endpoint now requires a **One Call by Call** paid subscription and returns:

```json
{
  "cod": 401,
  "message": "Please note that using One Call 3.0 requires a separate subscription..."
}
```

To keep the application completely free while preserving all required weather functionality, the project will use the following APIs instead:

| API | Purpose | Free |
|------|----------|------|
| Current Weather | Current conditions | ✅ |
| 5 Day Forecast | Hourly forecast (3-hour intervals) | ✅ |
| Geocoding | City → Coordinates | ✅ |

---

# Existing Architecture

```
Presentation
      │
      ▼
 ViewModel
      │
      ▼
 Repository
      │
      ▼
 OneCall API
      │
      ▼
 OpenWeather
```

---

# New Architecture

```
Presentation
      │
      ▼
 ViewModel
      │
      ▼
 Repository
      │
      ▼
 Weather API
      │
      ├──────────────┐
      ▼              ▼
Current Weather   Forecast
      │              │
      └──────┬───────┘
             ▼
      Combined UI State
```

---

# APIs Used

## Current Weather

### Endpoint

```
GET /data/2.5/weather
```

### Purpose

Returns

- Current temperature
- Humidity
- Pressure
- Wind speed
- Visibility
- Weather description
- Icon
- City name

### Example

```
GET https://api.openweathermap.org/data/2.5/weather
    ?lat=28.6139
    &lon=77.2090
    &units=metric
    &appid=API_KEY
```

---

## Forecast API

### Endpoint

```
GET /data/2.5/forecast
```

### Purpose

Returns

- 3-hour forecast
- 5-day forecast
- Weather icons
- Temperature trend

### Example

```
GET https://api.openweathermap.org/data/2.5/forecast
    ?lat=28.6139
    &lon=77.2090
    &units=metric
    &appid=API_KEY
```

---

## Geocoding API

### Endpoint

```
GET /geo/1.0/direct
```

### Example

```
GET /geo/1.0/direct
?q=Delhi
&limit=1
&appid=API_KEY
```

Returns

```
City Name
Latitude
Longitude
Country
State
```

---

# Retrofit Changes

## Old

```kotlin
@GET("data/3.0/onecall")
```

---

## New

```kotlin
@GET("data/2.5/weather")
suspend fun getCurrentWeather(...)
```

```kotlin
@GET("data/2.5/forecast")
suspend fun getForecast(...)
```

---

# Repository Changes

Old

```
Repository
    │
    ▼
OneCall()
```

New

```
Repository
      │
      ├──────────────┐
      ▼              ▼
CurrentWeather()  Forecast()
```

---

# ViewModel Changes

Instead of loading one endpoint, the ViewModel loads two endpoints.

```
ViewModel

      │

      ├───────────────┐

      ▼               ▼

Current Weather    Forecast

      │               │

      └───────┬───────┘

              ▼

        UI State
```

---

# UI Mapping

## Home Screen

Current Weather API provides

- City
- Temperature
- Weather Condition
- Weather Icon
- Humidity
- Pressure
- Wind Speed
- Visibility

---

## Forecast Screen

Forecast API provides

- Next 3 hours
- Next 6 hours
- Next 9 hours
- Next 12 hours
- Next 5 Days

---

# DTO Changes

## CurrentWeatherResponse

Contains

```
weather

main

wind

visibility

name
```

---

## ForecastResponse

Contains

```
list

city
```

Each list item contains

```
Date

Temperature

Humidity

Weather

Icon
```

---

# Repository Flow

```
UI

│

▼

ViewModel

│

▼

Repository

│

├──────────────┐

▼              ▼

Weather API   Forecast API

│              │

└───────┬──────┘

▼

Combined Result

│

▼

UI
```

---

# Error Handling

Handle

- No Internet
- Timeout
- Invalid API Key
- API Limit
- Unknown Exception

Repository returns

```
Loading
Success
Error
```

instead of throwing exceptions directly.

---

# Offline Strategy

Room database stores

- Current Weather
- Forecast

If network fails

```
Room Database

↓

Cached Weather

↓

UI
```

---

# Background Sync

WorkManager refreshes weather every

```
Every 1 Hour
```

Workflow

```
WorkManager

↓

Repository

↓

Weather API

↓

Room

↓

UI
```

---

# BuildConfig

Store API key securely.

```
BuildConfig.OPEN_WEATHER_KEY
```

Never hardcode

```
"YOUR_API_KEY"
```

inside the repository or ViewModel.

---

# Benefits of Migration

| Feature | One Call 3.0 | New Approach |
|----------|--------------|--------------|
| Free | ❌ | ✅ |
| Current Weather | ✅ | ✅ |
| Forecast | ✅ | ✅ |
| Paid Subscription | Required | No |
| MVVM Compatible | ✅ | ✅ |
| Repository Pattern | ✅ | ✅ |
| Offline Cache | ✅ | ✅ |
| WorkManager | ✅ | ✅ |

---

# Future Enhancements

## Phase 2

- Air Pollution API
- UV Index
- Sunrise/Sunset animations
- Weather Alerts
- Location Search
- Multiple Saved Cities

---

## Phase 3

- Weather Radar
- Maps SDK
- AI Weather Summary
- Widgets
- Wear OS Support
- Dynamic Material You Themes

---

# Final Architecture

```
                  +----------------------+
                  |     Compose UI       |
                  +----------+-----------+
                             |
                             ▼
                  +----------------------+
                  |     ViewModel        |
                  +----------+-----------+
                             |
                             ▼
                  +----------------------+
                  |    Repository        |
                  +----------+-----------+
                             |
        +--------------------+--------------------+
        |                                         |
        ▼                                         ▼
+---------------------+              +----------------------+
| Current Weather API |              | Forecast API         |
+---------------------+              +----------------------+
        |                                         |
        +--------------------+--------------------+
                             |
                             ▼
                  +----------------------+
                  |   Room Database      |
                  +----------+-----------+
                             |
                             ▼
                  +----------------------+
                  |     Compose UI       |
                  +----------------------+

                   ▲
                   │
              WorkManager
          (Background Refresh)
```

---

# Conclusion

Migrating from **One Call 3.0** to the **Current Weather** and **Forecast** APIs removes the dependency on a paid subscription while preserving all essential weather functionality. The updated architecture remains aligned with modern Android development practices (MVVM, Repository, Room, WorkManager, Hilt, and Jetpack Compose) and is suitable for both production-ready applications and portfolio projects.