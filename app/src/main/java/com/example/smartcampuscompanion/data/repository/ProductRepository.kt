package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.remote.ApiService
import com.example.smartcampuscompanion.data.remote.dto.ProductDto

class ProductRepository(
    private val api: ApiService
) {

    suspend fun getProducts(): List<ProductDto> {
        return try {
            api.getProducts()
        } catch (e: Exception) {
            emptyList()
        }
    }
}