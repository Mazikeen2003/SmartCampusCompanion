# Changelog - Smart Campus Companion

## 2026-04-16
### Added
- **Project Foundation**: Initialized Phase 3 (Finals) project structure.
- **Firebase SDK**: Integrated Firebase BOM, Authentication, and Firestore dependencies.
- **Configuration**: Added `google-services.json` and registered SHA-1 fingerprints for team collaboration.
- **Hilt Setup**: Created `FirebaseModule.kt` for Dependency Injection of Firebase services.
- **Navigation Base**: Established `NavGraph.kt` and `Routes.kt` with dynamic `startDestination` (Auth session check).
- **UI Placeholders**: Added initial screens for `AddAnnouncementScreen.kt` and `SettingsScreen.kt`.

### Changed
- Refactored `MainActivity.kt` to handle Firebase initialization and session-based navigation.
- Updated `LoginScreen.kt` and `RegisterScreen.kt` from mock logic to support future Firebase integration.

### Fixed
- Resolved Gradle sync issues regarding Hilt and Firebase version compatibility.