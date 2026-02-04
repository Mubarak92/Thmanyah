# Thmanyah – Multi-Module Android Podcast App

Enterprise-grade multi-module Android app with **MVVM + Clean Architecture**.

## 🏗️ Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                           APP                                 │
│    ┌──────────┐  ┌──────────┐  ┌─────────────────────────┐   │
│    │   GMS    │  │   HMS    │  │   Navigation + DI       │   │
│    └──────────┘  └──────────┘  └─────────────────────────┘   │
└──────────────────────────────────────────────────────────────┘
                              │
         ┌────────────────────┼────────────────────┐
         ▼                    ▼                    ▼
   ┌───────────┐        ┌───────────┐        ┌───────────┐
   │  :feature │        │  :feature │        │  :domain  │
   │   :home   │        │  :search  │        │           │
   └───────────┘        └───────────┘        └───────────┘
         │                    │                    │
         └────────────────────┼────────────────────┘
                              ▼
                        ┌───────────┐
                        │   :data   │
                        └───────────┘
                              │
         ┌────────────────────┼────────────────────┐
         ▼                    ▼                    ▼
   ┌───────────┐        ┌───────────┐        ┌───────────┐
   │  :core    │        │  :core    │        │  :core    │
   │  :network │        │  :common  │        │  :design  │
   └───────────┘        └───────────┘        └───────────┘
```

## 📁 Modules

| Module | Description |
|--------|-------------|
| `:app` | Main app with GMS/HMS flavors |
| `:core:common` | BaseViewModel, Resource wrapper, extensions |
| `:core:network` | NetworkHandler, interceptors, SafeApiCall |
| `:core:design` | Theme, colors, typography |
| `:core:testing` | Test rules and fakes |
| `:domain` | Models, repository interfaces, use cases |
| `:data` | DTOs, mappers, API, repository implementations |
| `:feature:home` | Home screen with infinite scroll |
| `:feature:search` | Search with debounce |

## 🔧 Key Patterns

### MVVM with StateFlow
```kotlin
class HomeViewModel : BaseViewModel<HomeUiState>(HomeUiState()) {
    fun refresh() { ... }
    fun loadMore() { ... }
}
```

### Resource Wrapper
```kotlin
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}
```

### NetworkHandler
```kotlin
NetworkHandler.instance
    .addInterceptor(HeaderInterceptor())
    .setup(NetworkConfig(baseUrl = "...", isDebug = true))
    .create<ThmanyahApi>()
```

## 📱 Product Flavors

- **gms** → Google Play Services
- **hms** → Huawei Mobile Services

```bash
./gradlew assembleGmsDebug
./gradlew assembleHmsRelease
```

## 🚀 Tech Stack

- Kotlin 2.0
- Jetpack Compose + Material 3
- Hilt DI
- Retrofit + OkHttp
- Coroutines + Flow
- Coil
- JUnit + MockK + Turbine
