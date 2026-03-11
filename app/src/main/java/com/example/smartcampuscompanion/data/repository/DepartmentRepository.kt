package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.DepartmentDao
import com.example.smartcampuscompanion.data.entity.Department
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DepartmentRepository @Inject constructor(
    private val departmentDao: DepartmentDao
) {
    val allDepartments: Flow<List<Department>> = departmentDao.getAll()
}
