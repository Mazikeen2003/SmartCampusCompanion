package com.example.smartcampuscompanion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartcampuscompanion.data.database.AppDatabase
import com.example.smartcampuscompanion.data.repository.TaskRepository
import com.example.smartcampuscompanion.ui.screens.tasks.AddEditTaskScreen
import com.example.smartcampuscompanion.ui.screens.auth.LoginRegisterScreen
import com.example.smartcampuscompanion.ui.screens.dashboard.DashboardScreen
import com.example.smartcampuscompanion.ui.screens.tasks.TaskListScreen
import com.example.smartcampuscompanion.ui.viewmodel.AuthViewModel
import com.example.smartcampuscompanion.ui.viewmodel.TaskViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    // Manual injection of TaskViewModel
    val database = AppDatabase.getDatabase(context)
    val repository = TaskRepository(database.taskDao())
    val taskViewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (authViewModel.isLoggedIn()) Routes.DASHBOARD else Routes.LOGIN_REGISTER
    ) {
        composable(Routes.LOGIN_REGISTER) {
            LoginRegisterScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN_REGISTER) { inclusive = true } } },
                onRegisterSuccess = { navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.LOGIN_REGISTER) { inclusive = true } } }
            )
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN_REGISTER) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavigateToTasks = { navController.navigate(Routes.TASK_LIST) }
            )
        }
        composable(Routes.TASK_LIST) {
            val taskViewModel: TaskViewModel = viewModel(factory = taskViewModelFactory)
            TaskListScreen(
                viewModel = taskViewModel,
                onAddClick = { navController.navigate(Routes.ADD_TASK) },
                onEditClick = { taskId -> navController.navigate(Routes.editTask(taskId)) }
            )
        }
        composable(Routes.ADD_TASK) {
            val taskViewModel: TaskViewModel = viewModel(factory = taskViewModelFactory)
            AddEditTaskScreen(
                viewModel = taskViewModel,
                onSaveDone = { navController.popBackStack() }
            )
        }
        composable(
            Routes.EDIT_TASK,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            val taskViewModel: TaskViewModel = viewModel(factory = taskViewModelFactory)
            AddEditTaskScreen(
                viewModel = taskViewModel,
                taskId = taskId,
                onSaveDone = { navController.popBackStack() }
            )
        }
    }
}
