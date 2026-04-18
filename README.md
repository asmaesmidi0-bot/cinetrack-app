# CineTrack: An Android Movie Discovery Application

## 1. Project Overview

CineTrack is a modern Android application designed for movie enthusiasts to discover, track, and manage their favorite films. The app provides a seamless user experience for browsing popular and top-rated movies, searching for specific titles, and viewing detailed information, including synopses, trailers, and community reviews. A key feature is the ability for users to save movies to a local "Favorites" list for easy access.

## 2. Key Code Files Table

This table highlights the essential files and architectural components of the CineTrack project.

| File Type | Example Names | Why It's Important |
| :--- | :--- | :--- |
| **Main Activity** | `MainActivity.kt` | The single entry point for the Android application. It hosts the `NavHost` composable, which manages all screen navigation within the app. |
| **API Service** | `TMDBService.kt` | A Retrofit interface that defines the HTTP endpoints for fetching movie data from The Movie Database (TMDB). This is crucial for all network operations. |
| **Database Helper**| `CineTrackDatabase.kt`| The Room database class that defines the local database schema and provides Data Access Objects (DAOs) for accessing stored data like favorite movies and user reviews. |
| **Adapter** | `MovieItem` (Composable)| In a modern Jetpack Compose UI, traditional `RecyclerView` adapters are replaced by composable functions. A function like `MovieItem` is used within a `LazyColumn` or `LazyGrid` to render a single item in a list, making the UI more declarative and efficient. |
| **Layout XML** | (Not Used) | This project is built entirely with Jetpack Compose. All UI elements are defined declaratively in Kotlin code, which eliminates the need for traditional XML layout files like `activity_main.xml`. |

## 3. Architecture and Technologies Used

CineTrack is built using modern, industry-standard technologies and follows the MVVM architecture pattern to ensure a scalable and maintainable codebase.

- **Architecture**: **MVVM (Model-View-ViewModel)** - Separates UI logic from business logic, improving testability and modularity.
- **UI**: **Jetpack Compose** - For building the app's user interface declaratively with Kotlin.
- **Dependency Injection**: **Hilt** - Manages dependencies throughout the app, simplifying the codebase and improving scalability.
- **Networking**: **Retrofit & Gson** - For making efficient and type-safe API calls and parsing JSON responses.
- **Asynchronous Programming**: **Kotlin Coroutines & Flow** - Manages background threads and handles asynchronous data streams reactively.
- **Database**: **Room** - Provides a local persistence library for storing user data like favorites and reviews.
- **Navigation**: **Navigation for Compose** - Manages all screen transitions and navigation within the app.
- **Image Loading**: **Coil** - An efficient image loading library for displaying movie posters and user profile pictures.

## 4. How to Build and Run the Project

Follow these steps to get the project running on your local machine.

1.  **Clone the Repository**
    ```bash
    git clone <YOUR_REPOSITORY_URL>
    ```
2.  **Get a TMDB API Key**
    - This project requires a free API key from The Movie Database (TMDB).
    - Create an account at [https://www.themoviedb.org/signup](https://www.themoviedb.org/signup).
    - Once registered, find your API key in your account settings under the "API" section.

3.  **Add API Key to Project**
    - In the root directory of the project, create a file named `local.properties` if it does not already exist.
    - Add the following line to this file, replacing `YOUR_API_KEY` with the key you obtained from TMDB:
    ```properties
    tmdb_api_key="YOUR_API_KEY"
    ```

4.  **Open and Build in Android Studio**
    - Open the project in the latest stable version of Android Studio.
    - Allow Gradle to sync and download all the required project dependencies.
    - Build and run the application on an Android emulator or a physical device.

## 5. Dependencies List

Here are some of the key dependencies used in CineTrack, showcasing the use of modern Android libraries:

- `androidx.activity:activity-compose:1.12.2`
- `androidx.compose.material3:material3`
- `androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0`
- `androidx.navigation:navigation-compose:2.7.7`
- `com.google.dagger:hilt-android:2.57.2`
- `androidx.room:room-runtime:2.8.4`
- `com.squareup.retrofit2:retrofit:2.9.0`
- `io.coil-kt:coil-compose:2.7.0`
