# Repository Guidelines

## Overall Rules
- Please answer in Japanese.
- Please provide a detailed explanation in Japanese.
- Please provide all the implementation code without omissions.
- Please clearly explain the justification and reason for the change, and the intention of the implementation.
- Please adhere to the basic principles of software development, such as the DRY principle, YAGNI, and SOLID principle. However, it is okay to prioritize the conventions of Android, Kotlin, iOS, and Swift over the principles.

## Project Structure & Module Organization
- `androidApp/`: Android app (Jetpack Compose, Hilt, DataStore). Entry points, activities, themes, and navigation live under `.../android/ui/`.
- `iosApp/`: iOS app (SwiftUI). Open `iosApp/iosApp.xcodeproj` and run the `iosApp` scheme.
- `shared/`: Kotlin Multiplatform shared code (Ktor client, SQLDelight, kotlinx.serialization). Database schema in `shared/src/commonMain/sqldelight/`.
- `gradle/`, `build.gradle.kts`, `settings.gradle.kts`: Centralized dependency versions via `gradle/libs.versions.toml`.
- `design/`: Project notes and architecture docs.

## Build, Test, and Development Commands
- Build all modules: `./gradlew build`
- Android debug APK: `./gradlew :androidApp:assembleDebug`
- Install on device/emulator: `./gradlew :androidApp:installDebug`
- Shared module only: `./gradlew :shared:build`
- Unit tests (JVM/common): `./gradlew test`
- Instrumented Android tests: `./gradlew connectedAndroidTest` (if `androidTest` exists)
- iOS: `open iosApp/iosApp.xcodeproj` → select `iosApp` scheme → run on a simulator or device.

## Coding Style & Naming Conventions
- Kotlin: Official style (`kotlin.code.style=official`). Use 4‑space indentation.
- Packages: lowercase (`jp.hotdrop.considercline...`).
- Classes/Composables: UpperCamelCase (e.g., `StartViewModel`, `PointUseConfirmScreen`).
- Functions/variables: lowerCamelCase.
- Compose: Keep UI state hoisted; suffix screens with `Screen`; preview names end with `Preview`.
- SQLDelight: Place `.sq` files under `shared/src/commonMain/sqldelight/`.
- Dependency Injection: Android uses Hilt with KSP; prefer constructor injection.

## Testing Guidelines
- Framework: `kotlin.test` in `shared`. Place tests under `shared/src/commonTest/`.
- Android: Add JVM tests in `androidApp/src/test/` and instrumented tests in `androidApp/src/androidTest/`.
- Run: `./gradlew test` (unit), `./gradlew connectedAndroidTest` (instrumented).
- Aim for meaningful coverage of business logic in `shared` and navigation/view‑model logic in `androidApp`.

## Commit & Pull Request Guidelines
- Commits: Use concise, present‑tense messages (scope optional), e.g., `android: fix Hilt setup` or `shared: add PointUse repository`.
- PRs: Include summary, modules touched, test steps, and screenshots for UI changes (Android/iOS). Link related issues.
- Keep changes small and focused; update `design/` docs if architecture decisions change.
  - `design/Android/`: android implementation Rules.
  - `desing/iOS/`: iOS implementation Rules.

## Security & Configuration Tips
- Do not commit secrets. Keep keys in `local.properties` (Android) or Xcode project settings (iOS). Add any new secrets to `.gitignore`.
- Network: Ktor client is configured per platform; prefer injecting endpoints and timeouts via the shared layer.

