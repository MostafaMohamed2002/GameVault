# AGENT.md — GameVault Coding Agent Guide

> This file provides structured context for AI coding agents (GitHub Copilot,
> Cursor, Claude, Gemini, etc.) contributing to the GameVault Android project.
> Read this before generating any code.

---

## Project Identity

- **App name**: Game Vault
- **Package**: `com.mostafadevo.freegames`
- **Purpose**: Android app that surfaces free PC games, game deals, and giveaways
  from APIs (CheapShark, GamerPower/FreeToGame) with filtering, search, and
  offline caching.
- **Play Store**: https://play.google.com/store/apps/details?id=com.mostafadevo.freegames
- **Language**: Kotlin
- **UI**: Jetpack Compose only
- **Min SDK**: 24 | **Target SDK**: 35

---

## Architecture — MVI (Model-View-Intent)

This project uses an MVI-style screen structure. Most screens follow a contract
pattern with state, events, effects, a ViewModel, and a root Composable.

| File | Role |
|---|---|
| `*UiState.kt` | Screen state container |
| `*UiEvent.kt` / `*Events.kt` | User actions / intents |
| `*UiEffect.kt` | One-shot side effects |
| `*ViewModel.kt` | State, business logic, and side effects |
| `*.kt` screen composable | UI only |

### Rules

1. Keep business logic in the ViewModel, not in Composables.
2. Use `StateFlow` for state.
3. Use `Channel` + `receiveAsFlow()` for one-shot effects.
4. Update state by copying the current state, not by mutating UI directly.
5. Composables should forward actions through callbacks or `onEvent(...)`.

### Known naming reality

Naming is mostly consistent, but not perfect yet:

- Some screens use `UiEvent`, others use `Events`.
- One screen folder is spelled `detailes_screen` in the current codebase.

Do not rename these unless the task explicitly asks for it.

---

## Package Structure

```
com.mostafadevo.freegames/
├── data/
│   ├── local/         ← Room entities, DAOs, database
│   ├── remote/        ← Retrofit APIs and DTOs
│   └── repository/    ← Concrete implementations of domain repository interfaces
├── domain/
│   ├── model/         ← Pure Kotlin data classes (no Android imports)
│   ├── mappers/       ← DTO/entity to domain mapping helpers
│   └── repository/    ← Interfaces only
├── ui/
│   ├── screens/      ← Screen Composables and ViewModels
│   ├── components/   ← Reusable Composables
│   └── theme/        ← MaterialTheme setup
├── di/               ← Hilt modules
├── analytics/        ← Analytics API and implementation
└── utils/            ← Helpers and constants
```

Layer rules:

- `domain/` must stay pure Kotlin
- `data/` depends on `domain/`
- `ui/` should depend on `domain/`, not `data/` directly
- `di/` wires the layers together

---

## Dependency Injection — Hilt

- Application class: `MyApp` annotated with `@HiltAndroidApp`
- ViewModels: `@HiltViewModel` with `@Inject constructor(...)`
- Repository and service wiring currently uses Hilt `@Module` classes with
  `@Provides`

Note: the codebase does **not** consistently use `@Binds` yet.

---

## Networking — Retrofit + Moshi

- JSON parsing: Moshi
- DTOs should follow API response shapes closely
- HTTP logging uses OkHttp logging interceptor
- Remote APIs are implemented with Retrofit
- Keep API keys and secrets out of source files

---

## Local Database — Room

- Database access uses Room
- DAOs live in `data/local/`
- Entity classes live in `data/local/`
- Domain models live in `domain/model/`
- Always map entities/DTOs to domain models before reaching UI

---

## Preferences — DataStore

- Preferences use DataStore
- Avoid `SharedPreferences` for new work
- Expose preference values through repositories as flows where possible

---

## Compose UI Conventions

1. Screen Composables should receive state and callbacks, not a ViewModel
   reference directly when the code already follows that pattern.
2. Keep UI code declarative and stateless where possible.
3. Reusable leaf components belong in `ui/components/`.
4. Prefer `MaterialTheme.colorScheme` and `MaterialTheme.typography` over
   hardcoded values.

---

## Code Style

- Kotlin only for new code
- Prefer small, focused changes
- Match existing file names and package layout
- Use Timber for logging where logging is needed
- Do not add new architecture patterns without checking existing ones first

---

## What NOT to Do

| ❌ Don't | ✅ Do instead |
|---|---|
| Call a repository from a Composable | Call it from the ViewModel |
| Put Android-specific code in `domain/` | Keep `domain/` pure Kotlin |
| Use `SharedPreferences` for new work | Use DataStore |
| Add XML layouts | Use Jetpack Compose |
| Hardcode secrets/API keys | Keep them out of source files |
| Use `LiveData` for new patterns | Prefer `StateFlow` / `Flow` |

---

## Adding a New Screen — Checklist

- [ ] Create a new package under `ui/screens/your_screen_name/`
- [ ] Add `YourScreenUiState.kt` (data class with defaults)
- [ ] Add `YourScreenUiEvent.kt` or `YourScreenEvents.kt` (sealed class)
- [ ] Add `YourScreenUiEffect.kt` (sealed class)
- [ ] Add `YourScreenViewModel.kt` (`@HiltViewModel`)
- [ ] Add `YourScreen.kt` (root Composable)
- [ ] Register the route in `AppNavigation.kt`
- [ ] Add any top-level nav entry if needed

---

## Adding a New API Integration — Checklist

- [ ] Add DTO data classes in `data/remote/`
- [ ] Add Retrofit interface in `data/remote/`
- [ ] Add domain model in `domain/model/`
- [ ] Add mapper function in `domain/mappers/`
- [ ] Add repository interface in `domain/repository/`
- [ ] Add concrete implementation in `data/repository/`
- [ ] Bind the implementation in `di/`

---

## Testing Conventions

- Use the project’s existing test stack and match adjacent test files
- ViewModel tests should verify state and effects, not internal implementation
- Repository tests should prefer an in-memory Room database when applicable
- UI tests should use fake dependencies or fake state
- Test naming should describe the behavior under test

---

## Key Files Quick Reference

| Purpose | File |
|---|---|
| App entry point | `app/src/main/java/com/mostafadevo/freegames/ui/MainActivity.kt` |
| Navigation graph | `app/src/main/java/com/mostafadevo/freegames/ui/screens/AppNavigation.kt` |
| Hilt application class | `app/src/main/java/com/mostafadevo/freegames/MyApp.kt` |
| Free games ViewModel | `app/src/main/java/com/mostafadevo/freegames/ui/screens/home_screen/FreeGamesScreenViewModel.kt` |
| Deals ViewModel | `app/src/main/java/com/mostafadevo/freegames/ui/screens/deals_screen/DealsAndGiveawayScreenViewModel.kt` |
| Settings ViewModel | `app/src/main/java/com/mostafadevo/freegames/ui/screens/settings_screen/SettingsScreenViewModel.kt` |
| DI modules | `app/src/main/java/com/mostafadevo/freegames/di/` |
| Theme | `app/src/main/java/com/mostafadevo/freegames/ui/theme/` |

---

## Before making changes

1. Find the closest existing screen or feature.
2. Mirror its file layout and naming.
3. Keep the change aligned with the current MVI + Hilt + Compose patterns.
4. Verify the change against the actual codebase before assuming a pattern exists.
