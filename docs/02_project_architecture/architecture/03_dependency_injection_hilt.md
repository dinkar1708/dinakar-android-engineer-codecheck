# Architecture Specification: Dependency Injection with Hilt

## 1. Overview & Dependency Graph
The native Android presentation layer uses **Dagger Hilt** for compile-time dependency injection. Hilt manages component lifecycles, scopes dependencies, and enables seamless substitution of fake test doubles during unit testing.

```mermaid
graph TD
    App[CodeCheckApplication - @HiltAndroidApp]
    SC[SingletonComponent]
    VMC[ViewModelComponent]

    App --> SC
    SC --> NM[NetworkModule]
    SC --> RM[RepositoryModule]
    SC --> DM[DispatcherModule]

    NM -->|provides| HTTP[HttpClient / Android Engine]
    NM -->|provides| JSON[kotlinx.serialization.json.Json]
    NM -->|provides| API[GitHubApiService (:shared)]
    DM -->|provides| DISP[@IoDispatcher CoroutineDispatcher]
    RM -->|binds| REPO[GitHubRepository (:shared)]

    SC --> VMC
    VMC --> VM[SearchViewModel - @HiltViewModel]
```

---

## 2. Hilt Modules Breakdown

### 1. `NetworkModule.kt` (`SingletonComponent`):
Provides JSON serializer, configured Ktor `HttpClient` with `ContentNegotiation`, 15-second timeouts, and logging, and the shared `GitHubApiService`:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGitHubApiService(httpClient: HttpClient): GitHubApiService = GitHubApiService(httpClient)
}
```

### 2. `RepositoryModule.kt` (`SingletonComponent`):
Provides the `GitHubRepository` interface implementation, wiring the shared `DefaultGitHubRepository` with the injected `@IoDispatcher`:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideGitHubRepository(
        apiService: GitHubApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): GitHubRepository = DefaultGitHubRepository(apiService, ioDispatcher)
}
```

### 3. `DispatcherModule.kt` (`SingletonComponent`):
Defines typed qualifiers (`@IoDispatcher`, `@DefaultDispatcher`, `@MainDispatcher`) allowing ViewModels and Repositories to inject `Dispatchers.IO` in production and `StandardTestDispatcher` in unit tests.

---

## 3. Testability via Hilt & Constructor Injection
Every ViewModel and Repository relies strictly on **constructor injection**:
```kotlin
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val gitHubRepository: GitHubRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel()
```
Because dependencies are constructor-injected, unit tests do **NOT** require launching Hilt test rules or Android emulators; simple fake instances (`FakeRepository`) are passed directly into the constructor.
