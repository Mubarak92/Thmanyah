# Thmanyah – Multi-Module Android Podcast App

Enterprise-grade multi-module Android app with **MVVM + Clean Architecture**.

## 📱 App Icon
![Group 1](https://github.com/user-attachments/assets/a594bde2-ab89-49f3-abeb-6d9920de6c69)<svg width="600" height="600" viewBox="0 0 600 600" fill="none" xmlns="http://www.w3.org/2000/svg">
<rect width="600" height="600" fill="black"/>
<path d="M212.2 508.65C265 443.15 286.3 377.55 295.6 309.75H304.2C313.5 377.55 334.8 443.15 387.6 508.65H392.8L481.7 332.95C378.2 274.85 336.3 194.95 306.8 91.35H293.2C263.6 194.95 221.8 274.85 118.3 332.95L207.2 508.65H212.2Z" fill="white"/>
</svg>

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

## 📲 Screen Shots

![Screenshot_1_Thmanyah](https://github.com/user-attachments/assets/0d21a99f-eb87-41af-9060-6f43232e7343)
![Screenshot_2_Thmanyah](https://github.com/user-attachments/assets/e56ca446-283f-488a-b1ef-34697ebf5032)


## 🚀 Tech Stack

- Kotlin 2.0
- Jetpack Compose + Material 3
- Hilt DI
- Retrofit + OkHttp
- Coroutines + Flow
- Coil
- JUnit + MockK + Turbine
