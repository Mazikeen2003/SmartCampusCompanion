package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.TaskDao
import com.example.smartcampuscompanion.data.entity.Task
import com.example.smartcampuscompanion.data.remote.FirebaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val remoteDataSource: FirebaseDataSource
) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
        .flowOn(Dispatchers.IO)
        .conflate()

    suspend fun refreshTasks() {
        try {
            val entities = remoteDataSource.getTasks()
            entities.forEach { taskDao.insertTask(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insert(task: Task) {
        try {
            val id = taskDao.insertTask(task)
            remoteDataSource.saveTask(task.copy(id = id.toInt()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun update(task: Task) {
        try {
            taskDao.updateTask(task)
            remoteDataSource.saveTask(task)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun delete(task: Task) {
        try {
            taskDao.deleteTask(task)
            remoteDataSource.deleteTask(task.id)
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
