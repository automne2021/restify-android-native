# Restify (Android Native - Jetpack Compose)

This is the native Android client for the Restify application, built entirely with Jetpack Compose.

## Getting Started

1.  Clone this repository.
2.  Open the project in Android Studio (Narwhal 2024.1.1 or newer is recommended).
3.  Wait for the Gradle sync to complete.
4.  Press "Run" to build and launch the app.

## Project Structure

-   **`/app/src/main/java/com/tennhom/restify/ui`**: The main directory for all UI-related code.
-   **`.../ui/navigation`**: Contains all navigation logic (e.g., `BottomNavigation`, `NavHost`).
-   **`.../ui/screens`**: Contains the code for the 5 main screens (`home`, `news`, `video`, `model3d`, `game`).
-   **`.../ui/theme`**: Contains the app's styling:
    -   `Type.kt` (Fonts / Typography)
    -   `Color.kt` (App Colors)
    -   `Theme.kt` (The main App Theme)
-   **`.../ui/components`**: Contains reusable Composables (e.g., `CustomButton`, `InfoCard`).

## Resources

-   **Fonts:** `Roboto Condensed` is pre-installed in `res/font`.
-   **Colors:** All app colors are defined in `res/values/colors.xml` (for Splash Screen) and `ui/theme/Color.kt` (for Compose).
-   **Images:** All vector and raster graphics are located in `res/drawable`.
-   **Strings:** All user-facing text is managed in `res/values/strings.xml` to support easy maintenance and localization.