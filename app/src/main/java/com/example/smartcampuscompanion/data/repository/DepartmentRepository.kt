package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.DepartmentDao
import com.example.smartcampuscompanion.data.entity.Department
import com.example.smartcampuscompanion.data.remote.ApiService
import com.example.smartcampuscompanion.data.remote.dto.DepartmentDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DepartmentRepository @Inject constructor(
    private val departmentDao: DepartmentDao,
    private val apiService: ApiService
) {
    val allDepartments: Flow<List<Department>> = departmentDao.getAll()

    suspend fun refreshDepartments() {
        try {
            val remoteDepartments = apiService.getDepartments()
            val entities = remoteDepartments.map { dto ->
                Department(
                    name = dto.name,
                    bgColor = dto.bgColor,
                    description = dto.description
                )
            }
            departmentDao.insertAll(entities)
        } catch (e: Exception) {
            // Handle error (e.g., log it or use fallback)
            e.printStackTrace()
        }
    }
}
// Handles remote-to-local synchronization for departments
