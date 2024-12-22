# FeelsLike Weather App ⛅️

![Code Coverage](https://img.shields.io/badge/Coverage-81%25-brightgreen.svg)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen.svg)
![Testing](https://img.shields.io/badge/Testing-Unit%20%7C%20UI%20%7C%20Screenshot-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Android-blue.svg)
![Languages](https://img.shields.io/badge/Languages-EN%20%7C%20ES-orange.svg)

FeelsLike is a modern Android weather application built with Jetpack Compose that provides weather information with an intuitive user interface.

## 🎯 Key Features

- **Real-time Weather Data**: Accurate weather information using WeatherAPI
- **Modern UI**: Built entirely with Jetpack Compose and Material 3
- **Robust Testing**: 81% code coverage with Unit, UI, and Screenshot tests
- **Multi-language**: Support for English and Spanish
- **Clean Architecture**: MVVM pattern with clear separation of concerns

## 📋 Table of Contents
- [Architecture & Technical Stack](#-architecture--technical-stack)
- [Libraries & Technologies](#-libraries--technologies)
- [Testing Capabilities](#-testing-capabilities)
- [Multi-language Support](#-multi-language-support)
- [Future Improvements](#-future-improvements)
- [Getting Started](#-getting-started)

## 🏗 Architecture & Technical Stack

### Clean Architecture + MVVM

```
📱 FeelsLike
├── 🎨 ui/
│   ├── screens/
│   ├── composables/
│   ├── viewmodel/
│   └── state/
├── 🏭 domain/
│   ├── model/
│   └── usecases/
└── 💾 data/
    ├── repository/
    └── source/
        ├── local/
        └── remote/
```

This architecture provides:
- 🎯 Clear separation of concerns
- 🧪 Highly testable code
- 🔧 Easy maintenance
- 📈 Scalable structure

## 📚 Libraries & Technologies

### UI & Jetpack
- **Jetpack Compose**: Modern declarative UI
- **Material 3**: Latest Material Design
- **Coil**: Efficient image loading
- **DataStore**: Modern preferences storage

### Networking & Data
- **Retrofit & OkHttp**: API communication
- **Gson**: JSON parsing
- **WeatherAPI**: Weather data source

### Dependency Injection
- **Hilt**: Modern DI for Android
  - Compile-time verification
  - Scoped dependencies
  - Testing utilities

### Async Programming
- **Coroutines**: Async operations
- **Flow**: Reactive streams

## 🧪 Testing Capabilities

Our comprehensive testing strategy ensures high-quality code:

- ✅ **81% Code Coverage**
  - Powered by Jacoco for detailed coverage reporting
  - HTML and XML reports generation
- ✅ **Unit Tests**
  - ViewModel testing with `kotlinx-coroutines-test`
  - Use cases and repositories with `mockk`
  - Flow and Coroutine testing
- ✅ **UI Tests**
  - Compose UI testing
  - Screenshot testing for pixel-perfect UI
  - Component integration tests

The project uses Jacoco for comprehensive code coverage analysis, enabling:
- Detailed coverage reports in both HTML and XML formats
- Coverage verification during builds
- Easy CI/CD integration
- Module-level and aggregate coverage reports
- Customizable coverage rules and thresholds

## 🌎 Multi-language Support
- 🇺🇸 English (default)
- 🇪🇸 Spanish

## 🚀 Future Improvements
- 📈 Expand test coverage beyond 81%
- 🌍 Add more languages
- 🔔 Weather notifications
- 🎯 Home screen widgets
- 📱 Offline support
- 🔄 Additional weather providers

## 🏁 Getting Started

### Prerequisites
- Android Studio Arctic Fox+
- JDK 11
- Android SDK 24+

### Building
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run the app

### Running Tests
```bash
# Unit tests
./gradlew test

# UI tests
./gradlew connectedAndroidTest

# Coverage report
./gradlew jacocoDebugCodeCoverage
```
