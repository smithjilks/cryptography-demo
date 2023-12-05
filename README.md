# Cryptography Demo Compose App and Spring Boot App

This repository showcases a cryptography demo with two separate directories—one for the Android app and another for the Spring Boot application.

## Android App (Compose)
The Android app is built using Jetpack Compose and incorporates the following technologies:

- **Dagger Hilt:** Dependency injection for Android.
- **Firebase Remote Config:** Dynamic configuration updates.
- **Build Types:** Different configurations for debug and release builds.
- **Chucker:** Network call logs for debugging.
- **Retrofit:** Making API calls.

## Spring Boot App (Kotlin)
The Spring Boot application, written in Kotlin, exposes essential endpoints for communication with the Android app. Key features include:

- **PostgreSQL Database:** Data storage and retrieval.
- **JWT (JSON Web Token):** Secure communication between the Android app and the Spring Boot app.
- **Spring Boot Security:** Ensuring secure access to endpoints.
- **Spring Boot Data JPA:** Simplifying data access with JPA.

## Demo Video
For a visual demonstration of the applications in action, please refer to the [demo video](https://github.com/smithjilks/cryptography-demo/blob/master/cryptography_demo.mp4).

Feel free to explore each directory for detailed implementation and configuration specifics.

**Note:** Ensure to set up the required configurations, such as database connections.
