# 🎓 Smart Campus Companion - Phase 3 (Finals)

## 📱 App Overview
The **Smart Campus Companion** is a comprehensive mobile ecosystem designed for modern university life. In the Final Phase, we have evolved into a cloud-integrated platform, transitioning from local-only storage to a high-performance **Firebase Firestore** backend with real-time synchronization and role-based access control.

### ✨ Key Features (Finals Update)
*   **🔐 Secure Authentication & Role-Based Access (RBAC):**
    *   **Firebase Auth Integration:** Secure login and registration.
    *   **Identity Management:** Automatic role detection (Admin vs. Student).
    *   **Permission-Based UI:** Dynamic interface adaptation. Admins gain "Add Announcement" capabilities, while Students receive a streamlined "Portal" view.
*   **☁️ Cloud-Sync Architecture (Hybrid Persistence):**
    *   **Firestore Integration:** Announcements and Tasks are synced to the cloud for multi-device access.
    *   **Room Database Offline Cache:** Full functionality even without internet connectivity.
    *   **Seamless Data Flow:** Repository-pattern implementation ensures data consistency between local and remote sources.
*   **⚙️ Advanced Settings & Personalization:**
    *   **Notification Control:** System for managing campus alerts.
    *   **Enhanced Dark Mode:** Refined Material 3 color palettes for better readability.
*   **🎨 UI/UX Polish:**
    *   **State-Aware UI:** Comprehensive loading indicators, error handling, and empty states.
    *   **Clean Layouts:** Professionally designed dashboard with fluid transitions and modern aesthetics.

---

## 👥 Team Composition & Roles
*   **Team Leader:** Bon Gershan - *Project oversight and architectural decisions.*
*   **Git Manager:** Biblanias, Iron L - *Gitflow management (Develop/Release/Main), PR enforcement, and v2.0-final release.*
*   **Backend & Data Integration:** Bermas, John Carlos - *Firebase/Firestore logic, Remote Data Source implementation, and Hybrid Sync strategy.*
*   **Feature & Logic Engineer:** Bermas, John Carlos - *Role-based logic, ViewModel state management, and core feature engineering.*
*   **UI/UX Developer:** Besa, Jennelyn - *Settings module, Material 3 theming, responsive layouts, and UX feedback loops.*

---

## 🏗️ Technical Architecture
The project utilizes a **Clean Architecture** approach with **MVVM** and a dedicated **Remote Data Layer**.

### 🛠️ Technical Focus
*   **Clean Data Layer:** Separation of concerns between `LocalDataSource` (Room) and `RemoteDataSource` (Firestore).
*   **Hilt Dependency Injection:** Scalable injection of Firebase services and Repository abstractions.
*   **Coroutine Flow:** Reactive data streams from Room/Firestore to the UI.
*   **Role Security:** Server-side role validation via Firestore security rules (simulated in logic).

---

## 🚀 Git Workflow & Requirements
*   **Gitflow Pattern:** `main`, `develop`, `release`, and `hotfix` branches.
*   **Versioning:** Milestone tags `v1.0-midterm` and `v2.0-final`.
*   **PR Workflow:** Mandatory reviews and conflict resolution documentation.

---

## 📝 Reflection

See the full reflection here:

[Reflection Document](reflection.md)

**Status:** ✅ Phase 2 Completed | **Target:** Finals Period Integration
