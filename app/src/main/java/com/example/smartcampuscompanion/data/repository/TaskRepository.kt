package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.TaskDao
import com.example.smartcampuscompanion.data.entity.Task
import com.example.smartcampuscompanion.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val apiService: ApiService
) {

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun refreshTasks() {
        try {
            val remoteTasks = apiService.getTasks()
            val entities = remoteTasks.map { dto ->
                Task(
                    id = dto.id,
                    title = dto.title,
                    description = dto.description,
                    dueDate = dto.dueDate,
                    dueTime = dto.dueTime,
                    isCompleted = dto.isCompleted
                )
            }
            // For tasks, we might want to be careful about overwriting local changes.
            // But for a simple sync, insertAll/replace works.
            entities.forEach { taskDao.insertTask(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)
    }
}
