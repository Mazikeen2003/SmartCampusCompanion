package com.example.smartcampuscompanion.data.remote
import com.example.smartcampuscompanion.data.remote.dto.AnnouncementDto
import com.example.smartcampuscompanion.data.remote.dto.DepartmentDto
import com.example.smartcampuscompanion.data.remote.dto.ProductDto
import com.example.smartcampuscompanion.data.remote.dto.TaskDto
import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @GET("departments")
    suspend fun getDepartments(): List<DepartmentDto>

    @GET("tasks")
    suspend fun getTasks(): List<TaskDto>

    @GET("announcements")
    suspend fun getAnnouncements(): List<AnnouncementDto>
}