# 📡 Offline-First Architecture

# Weather Intelligence

**Version:** 1.0

**Architecture:** Clean Architecture + Offline-First

**Database:** Room

**Synchronization:** WorkManager

---

# Table of Contents

1. Overview
2. What is Offline-First?
3. Design Principles
4. Single Source of Truth
5. Data Flow
6. Cache Strategy
7. Repository Decision Flow
8. Synchronization Strategy
9. Manual Refresh
10. Background Refresh
11. Cache Invalidation
12. Failure Handling
13. Edge Cases
14. Performance Considerations
15. Future Improvements

---

# 1. Overview

Weather Intelligence is designed using an **Offline-First Architecture**.

Unlike traditional applications that fetch data from the network on every launch, this application prioritizes locally cached data.

The local database is always queried first.

Benefits:

* Instant startup
* Works without Internet
* Lower battery consumption
* Reduced API usage
* Better user experience

---

# 2. What is Offline-First?

Offline-First means:

The application behaves as though the local database is the primary data source.

Remote APIs are only used to **refresh** the database.

Users always interact with cached data.

---

# 3. Design Principles

The Offline-First implementation follows:

* Single Source of Truth
* Repository Pattern
* Reactive Streams (Flow)
* Smart Cache TTL
* Background Synchronization
* Eventual Consistency

---

# 4. Single Source of Truth (SSOT)

The Room database is the only source observed by the UI.

```text id="1j8c0p"
Weather API

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

Never:

```text id="8n0u5y"
API

↓

Compose UI
```

This guarantees a consistent user experience, even when the network is unavailable.

---

# 5. Data Flow

```text id="v91u7k"
User Opens App

↓

Repository

↓

Read Room

↓

Display Cached Weather

↓

Check Cache TTL

↓

Expired?

├── No
│
│   Continue
│
└── Yes
     ↓
Call API

↓

Save Room

↓

Flow Emits

↓

UI Updates
```

The user sees cached content immediately while fresh data loads in the background.

---

# 6. Cache Strategy

Each dataset has its own Time-To-Live (TTL).

| Data                 | TTL        |
| -------------------- | ---------- |
| Current Weather      | 15 Minutes |
| Hourly Forecast      | 1 Hour     |
| Daily Forecast       | 3 Hours    |
| Air Quality (Future) | 1 Hour     |

TTL values can be adjusted without affecting the Presentation layer.

---

# 7. Repository Decision Flow

The Repository decides whether fresh data is required.

```text id="tb1v7a"
Request Weather

↓

Read Cache

↓

Cache Exists?

├── No
│
│   Fetch API
│
└── Yes
     ↓
Check TTL

↓

Expired?

├── No
│
│ Return Cache
│
└── Yes
     ↓
Fetch API

↓

Save Room

↓

Return Updated Flow
```

This keeps business logic centralized.

---

# 8. Synchronization Strategy

Synchronization occurs in two ways:

### Foreground

* User opens app
* User performs pull-to-refresh
* User changes city

### Background

* Periodic WorkManager
* Network becomes available
* Scheduled refresh

---

# 9. Manual Refresh

Pull-to-refresh bypasses the cache.

Flow:

```text id="j0m4sd"
Pull to Refresh

↓

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

The UI always observes Room.

---

# 10. Background Refresh

WorkManager periodically checks cache status.

```text id="2h1m9v"
Worker Starts

↓

Network Available?

↓

Yes

↓

Check TTL

↓

Expired?

↓

Download Latest Weather

↓

Update Room

↓

Flow Emits

↓

UI Refreshes Automatically
```

Constraints:

* Network connected
* Battery not low
* Exponential retry on transient failures

---

# 11. Cache Invalidation

Cache is invalidated when:

* TTL expires
* User changes city
* User performs manual refresh
* API schema changes (future migration)

Expired records may be cleaned periodically to reduce storage usage.

---

# 12. Failure Handling

## No Internet

* Display cached data
* Show offline indicator
* Skip refresh

## API Failure

* Keep existing cache
* Retry later

## Empty Cache + No Network

* Display friendly empty state
* Prompt user to retry when online

## Partial Update Failure

* Use Room transactions to prevent inconsistent data

---

# 13. Edge Cases

| Scenario                    | Expected Behavior                                                 |
| --------------------------- | ----------------------------------------------------------------- |
| App opened offline          | Show cached weather                                               |
| Cache expired + offline     | Show expired cache with last updated time                         |
| User refreshes offline      | Inform user, keep cache                                           |
| API returns incomplete data | Ignore invalid fields, preserve previous values where appropriate |
| Background sync fails       | Retry using WorkManager backoff policy                            |

---

# 14. Performance Considerations

To keep the app responsive:

* Read from Room before making network calls
* Use Kotlin Flow for automatic UI updates
* Perform database writes on `Dispatchers.IO`
* Batch related writes in a single transaction
* Avoid duplicate API requests for the same city
* Minimize unnecessary recompositions in Compose

---

# 15. Future Improvements

Potential enhancements:

* Stale-While-Revalidate strategy
* Delta synchronization
* Multiple weather providers with failover
* Sync prioritization based on user location
* Predictive prefetching for favorite cities
* Cloud synchronization for user preferences

---

# Offline-First Summary

The application follows a robust Offline-First architecture by combining:

* Room as the Single Source of Truth
* Repository Pattern
* Smart TTL-based caching
* Kotlin Flow for reactive updates
* WorkManager for background synchronization
* Transactional database updates
* Graceful failure handling

This approach provides a fast, resilient, and user-friendly experience while minimizing network usage and ensuring the application remains functional even without an internet connection.
