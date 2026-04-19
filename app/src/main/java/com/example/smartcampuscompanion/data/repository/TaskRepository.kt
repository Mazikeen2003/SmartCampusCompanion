package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.TaskDao
import com.example.smartcampuscompanion.data.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Siguraduhin na isa lang ang instance nito sa buong app
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val firestore: FirebaseFirestore
) {

    // Gamit ang flowOn(Dispatchers.IO) para masiguradong sa background thread ang database operations
    // Ang .conflate() ay para hindi mag-backlog ang UI kung sobrang bilis ng updates
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
        .flowOn(Dispatchers.IO)
        .conflate()

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
        try {
            taskDao.insertTask(task)
        } catch (e: Exception) {
            // Dito pwedeng mag-log ng error para sa debugging
            throw e
        taskDao.insertTask(task)
        try {
            firestore.collection("tasks").document(task.id.toString()).set(task).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun update(task: Task) {
        try {
            taskDao.updateTask(task)
        } catch (e: Exception) {
            throw e
        taskDao.updateTask(task)
        try {
            firestore.collection("tasks").document(task.id.toString()).set(task).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun delete(task: Task) {
        try {
            taskDao.deleteTask(task)
        } catch (e: Exception) {
            throw e
        taskDao.deleteTask(task)
        try {
            firestore.collection("tasks").document(task.id.toString()).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getTaskById(id: Int): Task? {
        return try {
            taskDao.getTaskById(id)
        } catch (e: Exception) {
            null
        }
    }
}