# Smart Campus Companion - Phase 3 Final Period

## Project Overview
The Smart Campus Companion is a mobile application designed to assist university students with viewing campus information, managing academic tasks, and reading campus announcements.

---

## 👥 Team Composition & Evolution (Prelim to Finals)

### MEMBER 1 – BIBLANIAS
**Role:** Git Manager & Release Engineer  
**Responsibilities (Prelim to Finals):**
*   **Prelim:** Established repository structure and basic Git workflow.
*   **Midterm:** Managed feature branches and resolved initial merge conflicts.
*   **Finals:** Enforced strict Gitflow (main, develop, release, hotfix), integrated all modules, handled final versioning (`v2.0-final`), and maintained `Changelog.md`.
*   **🔒 Ownership:** `navigation/*`, `MainActivity.kt`, Git management, and Release lifecycle.

---

### MEMBER 2 – BERMAS
**Role:** Backend & Data Integration Engineer  
**Responsibilities (Prelim to Finals):**
*   **Prelim:** Designed initial data models and mock data providers.
*   **Midterm:** Implemented local persistence using Room Database.
*   **Finals:** Migrated project to Firebase/Firestore, implemented real-time sync listeners, and optimized async data mapping from API to Entity.
*   **🔒 Ownership:** `data/remote/*`, `data/repository/*`, and core Firebase logic.

---

### MEMBER 3 – Besa
**Role:** Feature & Logic Engineer  
**Responsibilities (Prelim to Finals):**
*   **Prelim:** Developed core screen logic and basic authentication flow.
*   **Midterm:** Refactored project to MVVM architecture with ViewModels.
*   **Finals:** Implemented Role-Based Access Control (Admin/Student logic), Google Sign-In authentication flow, and robust State management (`StateFlow`).
*   **🔒 Ownership:** `ui/viewmodel/*` and business logic across all modules.

---

### MEMBER 4 – Bon
**Role:** UI/UX Engineer  
**Responsibilities (Prelim to Finals):**
*   **Prelim:** Created initial Jetpack Compose layouts and themes.
*   **Midterm:** Developed complex UI components for Task and Announcement lists.
*   **Finals:** Polished the app to Material 3 High-Fidelity standards, implemented Dark Mode, refined loading/error/empty states, and designed the branding assets.
*   **🔒 Ownership:** `ui/screens/*`, `ui/components/*`, and `theme/*`.

---

## 🚀 Key Final Features
*   **Google Sign-In:** One-tap secure login with auto-role provisioning.
*   **Real-time Notifications:** Instant alerts for new campus announcements.
*   **Role-Based Dashboards:** Distinct visual identities for Admin vs. Student accounts.
*   **Hybrid Sync:** Offline capability with Room and cloud sync with Firestore.

---

## 🛠️ Setup Instructions
1.  Add your `google-services.json` to the `app/` directory.
2.  Ensure you have a valid SHA-1 fingerprint in the Firebase Console.
3.  Build and run using Android Studio Ladybug or later.
