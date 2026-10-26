# Changelog

## 2.0.0 — 2026-10-26

Modernization of the original Full Sail styles-and-themes assignment
(v1.0). The app shows the same thing — a preview frame with a sword
and a name, plus background-color, text, text-color, and bold controls
— but as a Compose screen driven by one state model.

This public repository is a squashed export of that modernization:
the code and tests below are the v2.0.0 tree, in a single commit,
without the earlier private history.

### Fixed

- Rotation state: v1 saved the preview text in `onSaveInstanceState`
  but never read it back, and never saved the colors or bold at all,
  so rotating reset the preview. All four choices now live in a
  `ViewModel` mirrored into `SavedStateHandle`, so rotation and
  process recreation restore them.
- Style clash: v1 applied each choice with its own `setTextAppearance()`
  call, which replaced the whole style — picking a text color wiped
  out bold, and toggling bold wiped out the color. Background color,
  text color, and bold are now independent fields of one `PreviewState`,
  so they compose.
- Text-based switching: v1 switched on the radio buttons' displayed
  text ("Red"/"Green"/"Blue"). Colors are now an enum, so a label
  change or translation cannot break the logic.

### Changed

- Rewrote in Kotlin with Jetpack Compose and Material 3; the XML
  layouts, AppCompat Activity, ConstraintLayout, and `styles.xml`
  are gone. Portrait and landscape are one adaptive screen instead
  of two layouts, at v1's 200dp / 275dp preview sizes.
- Replaced v1's `buster_sword.png` asset with an original sword drawn
  with Canvas shapes.
- Renamed the application ID and package from
  `com.example.lyonsjustin_ce03` to `com.enonymous1.stylepreview`,
  and the app name to Style Preview.
- Toolchain: AGP 8.13.0, Gradle 8.14.3, Kotlin 2.2.0, Java 17,
  compile/target SDK 36, minSdk 24, Kotlin DSL build scripts, and a
  version catalog (`gradle/libs.versions.toml`).
- Release builds now use R8 shrinking (`minifyEnabled`,
  `shrinkResources`).
- Removed the assignment TODO comments and the stale "GitHub
  Preparation" instructions from the README.

### Added

- Real tests: v1's initial screen, placeholder behavior, each update
  preserving the other fields (including both directions of the
  style-clash regression), ViewModel updates, and restoration from
  `SavedStateHandle`, plus a package-name instrumentation test and
  deterministic Compose UI tests for typing, color, and bold.
- GitHub Actions CI: unit tests plus a debug build on every push and
  pull request to `main`.
- MIT LICENSE, this changelog, and a `.gitattributes` file normalizing
  line endings to LF.

### Notes and non-goals

- The preview colors keep v1's exact values, including its "blue"
  (`#311B92`, a deep indigo) — that is fidelity to the original, not
  a mistake.
- This is a styling demo, not a name generator or a game asset: the
  sword is a simple original drawing, and no game artwork is used.

## 1.0 — 2026-09-06

Original release: Java 8, AGP 8.0.2, SDK 33, a single Activity hosting
portrait and landscape XML layouts. A preview FrameLayout held a sword
image and a text label; radio groups chose background and text colors,
an EditText set the text, and a Switch toggled bold.
