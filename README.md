# Style Preview

A small Android app for previewing a styled name: type a sword name,
pick a background color, pick a text color, and toggle bold — and watch
the preview update live. The preview keeps its state across rotation
and process recreation, and background color, text color, and bold are
independent, so you can have bold red text (which v1 could not do).

Originally a Full Sail assignment in Java and XML; v2.0 rewrote it in
Kotlin with Compose and a ViewModel. See [CHANGELOG.md](CHANGELOG.md)
for what changed and what was deliberately left out.

This public repository is a squashed export of that v2.0.0
modernization: a single commit, with the earlier private history
intentionally not included.

## Tech stack

- Kotlin 2.2.0, Jetpack Compose, Material 3
- ViewModel + StateFlow, `SavedStateHandle` for process recreation
- Android Gradle Plugin 8.13.0, Gradle 8.14.3 (via the wrapper), JDK 17
- compile/target SDK 36, minSdk 24
- JUnit 4, Compose UI tests

The application ID is `com.enonymous1.stylepreview`.

## Features

- Type a name and see it in the preview frame immediately; while the
  field is empty the preview shows the "Insert Sword Name" placeholder.
- Background color: red, green, or blue (v1's exact color values).
- Text color: red, green, or blue, independent of the background.
- Bold toggle, independent of the text color.
- Portrait and landscape in one adaptive screen: the preview sits
  above the form in portrait and beside it in landscape, at the same
  200dp / 275dp sizes v1 used.
- All four preview choices survive rotation and process recreation.
- The sword in the preview is an original Canvas drawing, not v1's
  image asset.

## Folder structure

```text
style-preview/
├── .github/workflows/android.yml     # CI: unit tests + debug build
├── app/
│   ├── build.gradle.kts             # App module configuration
│   ├── proguard-rules.pro           # Release shrinker rules
│   └── src/
│       ├── androidTest/             # Instrumentation + Compose UI tests
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/enonymous1/stylepreview/
│       │   │   ├── MainActivity.kt      # Compose host, edge-to-edge
│       │   │   ├── PreviewScreen.kt     # Material 3 UI + sword drawing
│       │   │   ├── PreviewViewModel.kt  # Preview state, SavedStateHandle
│       │   │   └── PreviewState.kt      # Pure state model + updates
│       │   └── res/                 # Strings, themes, icons, backup rules
│       └── test/                    # Local JVM unit tests
├── gradle/
│   ├── libs.versions.toml           # Version catalog
│   └── wrapper/                     # Gradle Wrapper configuration
├── build.gradle.kts                 # Root plugin configuration
├── gradle.properties                # AndroidX and Gradle settings
├── gradlew / gradlew.bat            # Gradle Wrapper scripts
└── settings.gradle.kts              # Project and module registration
```

## Requirements

- Android Studio with Android SDK 36 installed
- JDK 17 (Android Studio's bundled JDK works)
- An Android emulator or physical device running API 24 or newer

## Setup and run

1. Clone the repository and open the project directory in Android Studio.
2. Let Android Studio sync the Gradle project and install any requested
   SDK components.
3. Select an emulator or connected Android device.
4. Run the `app` configuration from Android Studio.

From a terminal at the project root, the debug build can also be created with:

```powershell
.\gradlew.bat assembleDebug
```

The generated APK is written to `app/build/outputs/apk/debug/app-debug.apk`.
For a release build, use `assembleRelease` (R8 shrinking is enabled).

## Testing

Run local unit tests with:

```powershell
.\gradlew.bat testDebugUnitTest
```

Run instrumented tests (including the Compose UI tests) on a connected
device or emulator with:

```powershell
.\gradlew.bat connectedDebugAndroidTest
```
