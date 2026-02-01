package com.example.smartcampuscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcampuscompanion.ui.Screens.AuthViewModel
import com.example.smartcampuscompanion.ui.Screens.DashboardScreen
//import com.example.smartcampuscompanion.ui.Screens.DashboardScreen
import com.example.smartcampuscompanion.ui.Screens.LoginRegisterScreen
import com.example.smartcampuscompanion.ui.theme.SmartCampusCompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCampusCompanionTheme(darkTheme = false) {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                NavHost(navController = navController, startDestination = "login_register") {
                    composable("login_register") {
                        LoginRegisterScreen(
                            authViewModel = authViewModel,
                            onLoginSuccess = { navController.navigate("dashboard") },
                            onRegisterSuccess = { navController.navigate("dashboard") }
                        )
                    }
                    composable("dashboard") {
                        DashboardScreen()
                    }
                }
            }
        }



    }
}
