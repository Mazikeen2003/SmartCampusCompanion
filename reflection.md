## 📊 Architecture Diagram

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


## 📝 Reflection Document

### 🔍 Git Challenges & Learning Curve
One of the most significant challenges during this phase was the shift to a team-based development environment. Specifically, managing **Database Schema Changes** across multiple branches was difficult. When the `feature/task-manager` developer added a `Task` entity and the `feature/announcements` developer added an `Announcement` entity, the shared `AppDatabase.kt` became a hotspot for errors. We learned that communication is just as important as code—notifying the team before changing shared configuration files is vital.

### ⚔️ Conflict Resolution Case Study
We encountered a major merge conflict in `NavGraph.kt` and `AppDatabase.kt` during the final integration.

**The Conflict:**
Both features attempted to modify the same line in `AppDatabase` to include their respective DAOs and Entities. Additionally, the database version number was incremented differently on both branches.

**The Resolution:**
1.  **Stop & Sync:** We paused all active development on feature branches.
2.  **Manual Merge:** The Git Manager used the IDE's Merge Tool to manually accept both sets of entities and DAOs.
3.  **Standardization:** We agreed on a final version (`v12`) to ensure all migrations triggered correctly.
4.  **Verification:** We performed a clean build and verified that both the Task list and Announcement list were populating correctly before finalizing the merge.

---

### Merge Conflict Encountered During Feature Integration

During the integration phase, a merge conflict occurred when Bermas attempted to merge the feature/announcements branch into the develop branch. GitHub displayed a *"Resolve Conflicts"* notification, indicating that the branch could not be merged automatically.

The conflict occurred because multiple branches modified shared project files at the same time. Specifically, both the feature/task-manager and feature/announcements branches introduced changes related to database configuration and navigation setup.

Because these files were edited independently in different branches, Git could not determine which version should be kept during the merge.

To resolve the issue:

1. The Git Manager reviewed the conflicting files on GitHub.
2. The correct changes from both branches were manually combined.
3. The database configuration and navigation setup were updated to include both features.
4. After confirming that no functionality was lost, the conflict was resolved and the merge was completed successfully.

Although a screenshot of the GitHub conflict was not captured during the process, the conflict was documented and resolved through manual merging and verification of the application's build and functionality.

# 📝 Project Reflection

[⬅ Back to README](README.md)
