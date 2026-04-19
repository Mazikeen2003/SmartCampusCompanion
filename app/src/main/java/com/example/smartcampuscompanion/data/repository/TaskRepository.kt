package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.TaskDao
import com.example.smartcampuscompanion.data.entity.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val firestore: FirebaseFirestore
) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun refreshTasks() {
        try {
            val snapshot = firestore.collection("tasks").get().await()
            val entities = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Task::class.java)
            }
            entities.forEach { taskDao.insertTask(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
        try {
            firestore.collection("tasks").document(task.id.toString()).set(task).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
        try {
            firestore.collection("tasks").document(task.id.toString()).set(task).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
        try {
            firestore.collection("tasks").document(task.id.toString()).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)
    }
}
