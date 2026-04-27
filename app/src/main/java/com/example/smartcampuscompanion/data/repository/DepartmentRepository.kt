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
            // 1. Try to fetch from Cloud
            val snapshot = firestore.collection("departments").get().await()
            val entities = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Department::class.java)
            }
            
            if (entities.isNotEmpty()) {
                departmentDao.insertAll(entities)
            } else {
                // 2. Fallback to Local Defaults if Cloud is empty
                ensureDefaultDepartments()
            }
        } catch (e: Exception) {
            // 3. Fallback on Error
            ensureDefaultDepartments()
            e.printStackTrace()
        }
    }

    private suspend fun ensureDefaultDepartments() {
        val defaults = listOf(
            Department("College of Engineering", 0xFFFF0000, "Dean's Office: Room 301\nAvailable Courses: Civil, Mechanical, Electrical Engineering."),
            Department("School of Business", 0xFFFFFF00, "Location: Business Center\nAccredited by AACSB. Focus on Entrepreneurship."),
            Department("Information Technology", 0xFF008080, "Labs: 4th Floor Tech Hub\nSpecializations: Cybersecurity, Data Science, AI."),
            Department("Architecture & Design", 0xFFFFA500, "Studio: Arts Wing\nFocus on Sustainable Design and Urban Planning."),
            Department("College of Arts & Sciences", 0xFF800000, "Office: Humanities Hall\nDepartments: Psychology, Biology, Literature."),
            Department("School of Education", 0xFF0000FF, "Training: Lab School\nPrograms: Elementary and Secondary Education.")
        )
        departmentDao.insertAll(defaults)
    }
}
