package com.example.smartcampuscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.smartcampuscompanion.navigation.NavGraph
import com.example.smartcampuscompanion.theme.SmartCampusCompanionTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val auth = FirebaseAuth.getInstance()
        val startDestination = if (auth.currentUser != null) "dashboard" else "login_register"

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            
            SmartCampusCompanionTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    onThemeToggle = { isDarkMode = !isDarkMode },
                    startDestination = startDestination
                )
            }
        }
    }
}
