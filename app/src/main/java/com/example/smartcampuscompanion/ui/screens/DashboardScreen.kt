package com.example.smartcampuscompanion.ui.screens

import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcampuscompanion.ui.ProductViewModel

@Composable
fun DashboardScreen() {

    val viewModel: ProductViewModel = viewModel()

    val products = viewModel.products.collectAsState().value
    val loading = viewModel.loading.collectAsState().value
    val error = viewModel.error.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchProducts()
    }

    when {
        loading -> Text("Loading...")
        error.isNotEmpty() -> Text(error)
        else -> Column {
            products.take(5).forEach {
                Text(it.name)
            }
        }
    }
}