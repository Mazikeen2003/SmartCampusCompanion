package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.DepartmentDao
import com.example.smartcampuscompanion.data.entity.Department
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DepartmentRepository @Inject constructor(
    private val departmentDao: DepartmentDao,
    private val firestore: FirebaseFirestore
) {
    val allDepartments: Flow<List<Department>> = departmentDao.getAll()

    suspend fun refreshDepartments() {
        try {
            val snapshot = firestore.collection("departments").get().await()
            val entities = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Department::class.java)
            }
            departmentDao.insertAll(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
