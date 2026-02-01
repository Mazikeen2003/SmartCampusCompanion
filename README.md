## App Description
Smart Campus Companion is an Android mobile application designed to assist university students with accessing essential campus-related information in a simple and organized way. For the Prelim phase, the application focuses on establishing the project foundation, basic user interface, and Git collaboration workflow.
The app allows users to log in using mock authentication, view a dashboard with navigation options, and access a campus information module that displays a static list of departments and their contact details. This phase emphasizes proper project structure, UI development using Jetpack Compose, and collaborative development using Git and GitHub.

## Team Roles (Prelim Phase)

- *Iron Biblanias – Git Manager & Integrator* 
  Responsible for repository setup, branch management, pull request reviews, and integrating all features into the develop branch. Ensures proper Git workflow and that all members meet commit requirements.

- *Gershan Carl Bon – UI Designer (Login & Theme)*  
  Responsible for applying the Material theme and designing the Login screen using Jetpack Compose, including input fields and buttons with proper layout and responsiveness.

- *John Carl Bermas – UI & Navigation Developer*  
  Responsible for creating the Dashboard screen and implementing app navigation between Login, Dashboard, and Campus Information screens.

- *Jennelyn Besa – Feature & UI Developer*  
  Responsible for implementing the Campus Information module, mock authentication logic, and session storage using SharedPreferences.

- *Iron Biblanias – UI Support & Enhancement Developer*  
  Responsible for creating reusable UI components and improving UI consistency and basic visual feedback across all screens.

---
## Git Workflow

The team follows a structured Git workflow to support parallel development and collaboration:

- **Main Branch (main)**  
  Protected branch containing stable code only. No direct commits are allowed.

- **Develop Branch (develop)**  
  Integration branch where completed features are merged after review.

- *Feature Branches*  
  Each member works on a separate feature branch created from develop (example: feature/login-ui, feature/dashboard-ui).

### Workflow Steps:
1. Create a feature branch from develop
2. Commit changes frequently with meaningful commit messages
3. Push the feature branch to GitHub
4. Open a Pull Request targeting develop
5. Review and merge the Pull Request after successful build and basic testing

# Each team member contributes at least *five (5) commits* during the Prelim phase to demonstrate active participation and collaboration.
