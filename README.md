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

### 🛠️ Technical Focus
*   **State as Source of Truth:** A single immutable `UiState` object controls every pixel on the screen.
*   **Unidirectional Data Flow (UDF):** User actions flow up as Intents; data updates flow down as State.
*   **Local Persistence:** Room Database with version-controlled migrations for reliable data storage.
*   **Dependency Injection:** Powered by **Hilt** for scalable and testable code.

---

📋 Task Delegation

📋 Task Delegation
Prelim Tasks

👤 Member 1 – Git Manager & Light Integrator
Responsibilities: create repo & branches (main, develop), monitor commits, review/merge PRs, minimal coding (MainActivity, base structure)
Deliverables: stable develop branch, clean Git history, repo link

👤 Member 2 – UI Designer (Login & Theme)
Responsibilities: build Login UI (Jetpack Compose), apply Material Theme, handle UI states & responsiveness
Deliverables: Login screen, theme applied, ≥5 commits

👤 Member 3 – UI & Navigation Developer
Responsibilities: Dashboard UI, app navigation (Login → Dashboard → Campus Info), NavHost & navigation graph
Deliverables: Dashboard UI, functional navigation, ≥5 commits

👤 Member 4 – Feature & UI Developer
Responsibilities: Campus Info module (departments + contacts), mock authentication, store login session (SharedPreferences)
Deliverables: working login logic, populated campus info screen, ≥5 commits

👤 Member 5 – UI Support & Easy Tasks
Responsibilities: reusable UI components (buttons, cards, text styles), minor UI polish, consistency fixes
Deliverables: shared components, UI polish, ≥5 commits

Midterm Tasks – MVVM + Room Phase

General Rules: ≥8 meaningful commits per member; PRs required; only Git Manager edits navigation/MainActivity; final tag: v1.0-midterm

👤 Member 1 – Git Manager & Integrator (Biblanias)
Allowed Files: navigation/NavGraph.kt, navigation/Routes.kt, MainActivity.kt
Responsibilities: manage branches (room-core, task-manager, announcements), enforce PR workflow, merge features, ensure builds, document conflict, create release tag

👤 Member 2 – Room Core & Data Architecture Owner (Bon)
Branch: feature/room-core
Allowed Files: AppDatabase.kt, entities (User.kt, Department.kt), TypeConverters.kt
Responsibilities: configure Room DB, register DAOs, handle migrations, maintain clean data layer
Midterm Deliverables: architecture diagram, reflection (Git challenges + conflict resolution)

👤 Member 3 – Task Manager Feature Owner (Besa)
Branch: feature/task-manager
Allowed Files: Task entity, DAO, repository, ViewModel, Task screens (TaskListScreen, AddEditTaskScreen)
Responsibilities: full CRUD, Date/Time picker, LazyColumn rendering, connect UI → VM → Repo → DAO, empty state handling, maintain MVVM separation

👤 Member 4 – Announcements Feature Owner (Bermas)
Branch: feature/announcements
Allowed Files: Announcement entity, DAO, repository, ViewModel, Announcement screens
Responsibilities: display announcements, store in Room, mark-as-read feature, read/unread indicators, connect UI → VM → Repo → DAO, empty state handling



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
