# Changelog - Smart Campus Companion

## [2.0.0] - 2026-04-26 (Final Submission)

### Added
- **Google Sign-In**: Integrated secure one-tap authentication with automatic user registration.
- **Automated Real-Time Notifications**: Implemented a Firestore Snapshot Listener that instantly notifies students when an Admin posts a new announcement.
- **Notification Customization**: Added "Silent Mode" and "Push Toggle" in Settings to allow users to control alert behavior (ring vs. silent).
- **High-Fidelity UI Branding**: Designed and integrated a custom App Icon and polished the Dashboard/Login headers with the new branding assets.
- **Status Bar Color Match**: Implemented dynamic status bar color changing to match the Material 3 surface tones.

### Changed
- **Visual Identity Separation**: Redesigned the Admin and Student dashboards with distinct color schemes (System Red vs. Resource Blue) for clear role differentiation.
- **Dependency Management**: Fully migrated to Gradle Version Catalog (`libs.versions.toml`) for standardized and error-free builds.
- **README Overhaul**: Documented the full team evolution and detailed role delegations from Prelim to Finals.

### Fixed
- **Hilt MissingBinding**: Resolved Context injection issues in `AuthViewModel`.
- **Icon Deprecations**: Migrated all icons to the modern `AutoMirrored` Material 3 versions.
- **FCM Topic Lifecycle**: Fixed a bug where users remained subscribed to notifications after logging out.
- **Gradle Sync Errors**: Resolved KSP/Kotlin version mismatches to ensure a stable `BUILD SUCCESSFUL` state.

### Removed
- **Manual FCM Keys**: Cleaned up the project by removing unnecessary manual key configurations in favor of the automated `google-services.json` flow.
