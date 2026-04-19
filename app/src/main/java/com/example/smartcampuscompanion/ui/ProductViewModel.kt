package com.example.smartcampuscompanion.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.remote.RetrofitClient
import com.example.smartcampuscompanion.data.remote.dto.ProductDto
import com.example.smartcampuscompanion.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository(RetrofitClient.api)

    private val _products = MutableStateFlow<List<ProductDto>>(emptyList())
    val products: StateFlow<List<ProductDto>> = _products

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                _error.value = "Failed to load data"
            } finally {
                _loading.value = false
            }
        }
    }
}