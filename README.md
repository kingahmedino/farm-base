# Farmer Management App

A modern Android application for managing farmer profiles. Built with Jetpack Compose and following clean architecture principles.

## Features

- Create and manage farmer profiles
- Upload profile pictures
- Validate Nigerian phone numbers
- Handle specialty crop selections
- Material 3 Design implementation
- Offline-first architecture with local database storage

## Tech Stack

- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room
- **Image Loading**: Coil
- **Networking**: Retrofit with Gson
- **Image Upload**: ImgHippo API
- **Kotlin Coroutines & Flow** for asynchronous operations
- **Version Catalog** for dependency management

## Environment Setup

This project uses environment variables for sensitive configuration. To set up:

1. Create a `env.properties` file in your project root

2. Edit `env.properties` and configure it appropriately:
```properties
IMG_HIPPO_API_KEY="your_actual_api_key"
IMG_HIPPO_BASE_URL="https://api.imghippo.com/v1/"
```

Note: `env.properties` is not committed to version control to protect sensitive information.

## Project Setup

1. Clone the repository
2. Set up environment variables as described above
3. Open in Android Studio
4. Sync project with Gradle files
5. Run the app

## Architecture

The app follows MVVM architecture with Clean Architecture principles:

- **UI Layer**: Compose UI components and ViewModels
- **Domain Layer**: Use cases and business logic
- **Data Layer**: Repository pattern with Room database and network calls

## Key Components

### Farmer Profile
- Name
- Email
- Nigerian Phone Number (validated)
- Location
- Specialty Crops (dropdown selection)
- Profile Picture (optional)

### Database Schema
- Local Room database for offline-first functionality
- Automatic migration handling
- Efficient data querying

### API Integration
- Image upload to ImgHippo
- Error handling
- Loading state management

## Dependencies

All dependencies are managed through the version catalog (`libs.versions.toml`). Key dependencies include:

```toml
[versions]
compose-bom = "2024.04.01"
room = "2.6.1"
hilt = "2.51.1"
retrofit = "2.11.0"
coil = "2.5.0"

[libraries]
# Compose
compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
material3 = { group = "androidx.compose.material3", name = "material3" }

# Room
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.