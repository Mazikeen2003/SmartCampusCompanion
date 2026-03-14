# 🎓 Smart Campus Companion - Phase 2 (Midterm)

## 📱 App Overview
The **Smart Campus Companion** is a robust mobile solution designed to streamline the university experience. In Phase 2, we have transitioned from static layouts to a dynamic, data-driven architecture, focusing on personal organization and campus-wide communication.

### ✨ Key Features (Midterm Update)
*   **📅 Task & Schedule Manager:**
    *   **Full CRUD Operations:** Add, view, edit, and delete academic tasks.
    *   **Smart Scheduling:** Integrated Date and Time pickers driven by UI state.
    *   **Organized UI:** Tasks are rendered using `LazyColumn` with state-aware completion toggles and strike-through animations.
*   **📢 Campus Announcements Module:**
    *   **Persistent Updates:** Real-time campus news stored locally using Room Database.
    *   **Read/Unread Tracking:** Visual indicators (badges and opacity changes) for new announcements.
    *   **Detail View:** Rich text expanded views for deep-diving into campus events.
*   **🌓 Adaptive UI:**
    *   **Dynamic Theme Toggle:** Manual switch between Light and Dark modes.
    *   **Material 3 Design:** Professional gradients and elevated card layouts for a premium feel.

---

## 👥 Team Composition & Roles
*   **Team Leader:** Bon Gershan - *Project oversight and architectural decisions.*
*   **Git Manager:** Biblanias, Iron L - *Branch management, PR reviews, and conflict resolution.*
*   **UI/UX Developer:** Besa, Jennelyn - *Material 3 implementation and Dark Mode theming.*
*   **Feature Developer:** Bermas, John Carlos - *Room DB setup, DAOs, and MVI business logic.*
*   **QA / Documenter:** Bon Gershan - *Testing, Bug tracking, and README maintenance.*

---

## 🏗️ Technical Architecture
The project strictly implements **MVVM** enhanced with the **MVI (Model-View-Intent)** pattern to ensure predictable state management and high testability.

### 📊 Architecture Diagram
```mermaid
graph TD
    subgraph Presentation_Layer [Presentation Layer - Compose + MVI]
        UI[Jetpack Compose Screens]
        Intent[User Intents - TaskIntent/AnnIntent]
        VM[ViewModel - State Management]
        State[Immutable UI State - StateFlow]
        Effect[Side Effects - Channels/Navigation]
    end

    subgraph Domain_Data_Layer [Domain & Data Layer - Room + Repository]
        Repo[Repositories - Task/Announcement]
        DAO[Room DAOs]
        DB[(Room Database)]
        Entity[Data Entities]
    end

    UI -->|Triggers| Intent
    Intent -->|Handled by| VM
    VM -->|Updates| State
    State -->|Renders| UI
    VM -.->|Emits| Effect
    Effect -.->|Navigate/Snackbar| UI
    VM <-->|Requests| Repo
    Repo <-->|Accesses| DAO
    DAO <-->|CRUD| DB
```

### 🛠️ Technical Focus
*   **State as Source of Truth:** A single immutable `UiState` object controls every pixel on the screen.
*   **Unidirectional Data Flow (UDF):** User actions flow up as Intents; data updates flow down as State.
*   **Local Persistence:** Room Database with version-controlled migrations for reliable data storage.
*   **Dependency Injection:** Powered by **Hilt** for scalable and testable code.

---

## 🚀 Git Workflow & Requirements
*   **Primary Branches:** `main` (Production), `develop` (Integration).
*   **Feature Branches:** `feature/task-manager`, `feature/announcements`.
*   **Collaboration:** Used Pull Requests (PRs) for all merges into `develop`.
*   **Release Tag:** `v1.0-midterm`

---

## 📝 Reflection

See the full reflection here:

[Reflection Document](reflection.md)

**Status:** ✅ Phase 2 Completed | **Target:** Finals Period Integration
