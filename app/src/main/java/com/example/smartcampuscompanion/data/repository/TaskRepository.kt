package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.TaskDao
import com.example.smartcampuscompanion.data.entity.Task
import com.example.smartcampuscompanion.data.remote.FirebaseDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val remoteDataSource: FirebaseDataSource,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var syncJob: Job? = null

    init {
        startSync()
    }

    fun startSync() {
        syncJob?.cancel()
        val email = firebaseAuth.currentUser?.email ?: return
        syncJob = repositoryScope.launch {
            try {
                // Fetch role from Firestore to determine sync strategy
                val doc = firestore.collection("users").document(email).get().await()
                val role = doc.getString("role") ?: "student"
                val isAdmin = role == "admin"

                remoteDataSource.getTasksFlow(email, isAdmin).collect { remoteList ->
                    // For Admin, we might want to clear local DB first to avoid stale global tasks 
                    // if they switch accounts, but for a student project, replace is usually enough.
                    remoteList.forEach { taskDao.insertTask(it) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshUserSession() {
        startSync()
    }

    suspend fun clearLocalTasks() {
        taskDao.clearAllTasks()
    }

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
        .flowOn(Dispatchers.IO)
        .conflate()

    suspend fun refreshTasks() {
        val email = firebaseAuth.currentUser?.email ?: return
        try {
            val doc = firestore.collection("users").document(email).get().await()
            val role = doc.getString("role") ?: "student"
            val isAdmin = role == "admin"
            
            val entities = remoteDataSource.getTasks(email, isAdmin)
            entities.forEach { taskDao.insertTask(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insert(task: Task) {
        try {
            val id = taskDao.insertTask(task)
            // Ensure ownerEmail is set correctly
            val finalTask = task.copy(
                id = id.toInt(),
                ownerEmail = firebaseAuth.currentUser?.email ?: ""
            )
            remoteDataSource.saveTask(finalTask)
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
