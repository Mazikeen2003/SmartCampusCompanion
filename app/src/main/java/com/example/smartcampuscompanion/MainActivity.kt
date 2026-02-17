package com.example.smartcampuscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcampuscompanion.ui.screens.auth.LoginRegisterScreen
import com.example.smartcampuscompanion.ui.screens.dashboard.DashboardScreen
import com.example.smartcampuscompanion.theme.SmartCampusCompanionTheme
import com.example.smartcampuscompanion.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCampusCompanionTheme(darkTheme = isSystemInDarkTheme()) {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                NavHost(
                    navController = navController,
                    startDestination = if (authViewModel.isLoggedIn()) "dashboard" else "login_register"
                ) {
                    composable("login_register") {
                        LoginRegisterScreen(
                            authViewModel = authViewModel,
                            onLoginSuccess = { navController.navigate("dashboard") { popUpTo("login_register") { inclusive = true } } },
                            onRegisterSuccess = { navController.navigate("dashboard") { popUpTo("login_register") { inclusive = true } } }
                        )
                    }
                    composable("dashboard") {
                        DashboardScreen(onLogout = {
                            authViewModel.logout()
                            navController.navigate("login_register") {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        })
                    }
                }
            }
        }
    }
}
