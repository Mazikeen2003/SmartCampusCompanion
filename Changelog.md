# Changelog - Smart Campus Companion

## [2.0.0-rc1] - 2026-04-16

### Added
- **Role-Based UI Implementation**: Integrated conditional rendering in `DashboardScreen.kt`.
    - **Admin View**: Displays "ADMIN PANEL" header and enables the Floating Action Button (+) for announcement creation.
    - **Student View**: Displays "STUDENT PORTAL" with a read-only interface.
- **Firestore User Identity**: Implemented server-side role fetching in `AuthViewModel.kt`. The app now identifies user permissions (Admin/Student) directly from the Firestore `users` collection.
- **Hybrid Data Strategy (Room + Firestore)**:
    - Updated `AnnouncementRepository` and `TaskRepository` to support dual-persistence.
    - Announcements and Tasks are saved locally in **Room** for offline access and synced to **Firebase Firestore** for real-time cloud visibility.
- **Firebase SDK**: Fully integrated Firebase BOM, Authentication, and Firestore for production use.
- **Hilt Setup**: Created `FirebaseModule.kt` for Dependency Injection of Firebase services.

### Changed
- **Architectural Unification**: Successfully migrated the project from Retrofit/REST API to a pure **Firebase Firestore** backend.
- **Build Standardization**:
    - Upgraded Project SDK to **Level 35** and **Java 17** for Hilt and Room compatibility.
    - Added `kotlinx-coroutines-play-services` to support asynchronous Firebase tasks using `await()`.
- **Navigation Base**: Established `NavGraph.kt` with dynamic `startDestination` based on active Firebase Auth sessions.

### Fixed
- **Dagger/Hilt MissingBinding**: Resolved critical build errors by removing legacy `ApiService` and `RetrofitClient` dependencies.
- **State Synchronization**: Fixed a bug where Admin features would not appear immediately by implementing forced role-fetching upon login.
- **Merge Conflict Resolution**: Handled complex conflicts in `NavGraph.kt`, `build.gradle.kts`, and `Repository` files during team integration.

### Removed
- **Legacy Retrofit Code**: Deleted all unused DTOs, API interfaces, and Retrofit configurations (`ApiService.kt`, `RetrofitClient.kt`).
- **Mock Authentication**: Eliminated hardcoded login checks in favor of real Firebase Authentication.