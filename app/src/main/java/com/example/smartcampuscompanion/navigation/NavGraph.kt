package com.example.smartcampuscompanion.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartcampuscompanion.ui.screens.tasks.AddEditTaskScreen
import com.example.smartcampuscompanion.ui.screens.auth.LoginRegisterScreen
import com.example.smartcampuscompanion.ui.screens.dashboard.DashboardScreen
import com.example.smartcampuscompanion.ui.screens.tasks.TaskListScreen
import com.example.smartcampuscompanion.ui.screens.campus.CampusInfoScreen
import com.example.smartcampuscompanion.ui.screens.announcement.AnnouncementScreen
import com.example.smartcampuscompanion.ui.screens.announcement.AnnouncementDetailScreen
import com.example.smartcampuscompanion.ui.screens.announcement.AddAnnouncementScreen
import com.example.smartcampuscompanion.ui.screens.settings.SettingsScreen
import com.example.smartcampuscompanion.ui.viewmodel.AuthViewModel
import com.example.smartcampuscompanion.ui.viewmodel.TaskViewModel
import com.example.smartcampuscompanion.ui.viewmodel.AnnouncementViewModel
import com.example.smartcampuscompanion.ui.viewmodel.DepartmentViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    startDestination: String = Routes.LOGIN_REGISTER
) {
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN_REGISTER) {
            LoginRegisterScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN_REGISTER) { inclusive = true } } },
                onRegisterSuccess = { 
                    // Registration success is now handled via a dialog in RegisterScreen
                    // We don't navigate to Dashboard anymore.
                }
            )
        }
        composable(Routes.DASHBOARD) {
            val departmentViewModel: DepartmentViewModel = hiltViewModel()

            DashboardScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN_REGISTER) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavigateToTasks = { navController.navigate(Routes.TASK_LIST) },
                onNavigateToAnnouncements = { navController.navigate(Routes.ANNOUNCEMENT_LIST) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                onNavigateToAddAnnouncement = { navController.navigate(Routes.ADD_ANNOUNCEMENT) },
                viewModel = departmentViewModel,
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                userRole = "student"
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                isDarkMode = isDarkMode,
                onThemeToggle = onThemeToggle,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.ADD_ANNOUNCEMENT) {
            AddAnnouncementScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.TASK_LIST) {
            val taskViewModel: TaskViewModel = hiltViewModel()
            TaskListScreen(
                viewModel = taskViewModel,
                onAddClick = { navController.navigate(Routes.ADD_TASK) },
                onEditClick = { taskId -> navController.navigate(Routes.editTask(taskId)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.ADD_TASK) {
            val taskViewModel: TaskViewModel = hiltViewModel()
            AddEditTaskScreen(
                viewModel = taskViewModel,
                onSaveDone = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            Routes.EDIT_TASK,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            val taskViewModel: TaskViewModel = hiltViewModel()
            AddEditTaskScreen(
                viewModel = taskViewModel,
                taskId = taskId,
                onSaveDone = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.CAMPUS_INFO) {
            CampusInfoScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.ANNOUNCEMENT_LIST) {
            val announcementViewModel: AnnouncementViewModel = hiltViewModel()
            AnnouncementScreen(
                viewModel = announcementViewModel,
                onAnnouncementClick = { announcementId -> 
                    navController.navigate(Routes.announcementDetail(announcementId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            Routes.ANNOUNCEMENT_DETAIL,
            arguments = listOf(navArgument("announcementId") { type = NavType.IntType })
        ) { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getInt("announcementId") ?: 0
            val announcementViewModel: AnnouncementViewModel = hiltViewModel()
            AnnouncementDetailScreen(
                viewModel = announcementViewModel,
                announcementId = announcementId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
