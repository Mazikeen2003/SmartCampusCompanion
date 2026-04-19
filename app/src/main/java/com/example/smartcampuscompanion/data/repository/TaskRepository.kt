package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.TaskDao
import com.example.smartcampuscompanion.data.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Siguraduhin na isa lang ang instance nito sa buong app
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    // Gamit ang flowOn(Dispatchers.IO) para masiguradong sa background thread ang database operations
    // Ang .conflate() ay para hindi mag-backlog ang UI kung sobrang bilis ng updates
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
        .flowOn(Dispatchers.IO)
        .conflate()

    suspend fun insert(task: Task) {
        try {
            taskDao.insertTask(task)
        } catch (e: Exception) {
            // Dito pwedeng mag-log ng error para sa debugging
            throw e
        }
    }

    suspend fun update(task: Task) {
        try {
            taskDao.updateTask(task)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun delete(task: Task) {
        try {
            taskDao.deleteTask(task)
        } catch (e: Exception) {
            throw e
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