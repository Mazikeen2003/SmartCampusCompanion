package com.example.smartcampuscompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.smartcampuscompanion.navigation.NavGraph
import com.example.smartcampuscompanion.theme.SmartCampusCompanionTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import com.example.smartcampuscompanion.ui.ProductViewModel
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            
            SmartCampusCompanionTheme(darkTheme = isDarkMode) {
                val productViewModel: ProductViewModel = viewModel()

                val products = productViewModel.products.collectAsState().value
                val loading = productViewModel.loading.collectAsState().value
                val error = productViewModel.error.collectAsState().value

                LaunchedEffect(Unit) {
                    productViewModel.fetchProducts()
                }
                when {
                    loading -> Text("Loading...")
                    error.isNotEmpty() -> Text(error)
                    else -> Column {
                        products.take(3).forEach {
                            Text(it.name)
                        }
                    }
                }
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    isDarkMode = isDarkMode,
                    onThemeToggle = { isDarkMode = !isDarkMode }
                )
            }
        }
    }
}
