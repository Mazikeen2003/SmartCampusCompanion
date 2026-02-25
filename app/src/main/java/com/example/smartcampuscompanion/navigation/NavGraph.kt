package com.example.smartcampuscompanion.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartcampuscompanion.ui.screens.auth.LoginRegisterScreen
import com.example.smartcampuscompanion.ui.screens.dashboard.DashboardScreen
import com.example.smartcampuscompanion.ui.viewmodel.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()

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
                }
            )
        }
    }
}
