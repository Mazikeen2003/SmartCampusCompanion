package com.example.smartcampuscompanion.data.remote

import com.example.smartcampuscompanion.data.entity.Announcement
import com.example.smartcampuscompanion.data.entity.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Member 2 Logic: Listen for announcements in real-time
    fun getAnnouncementsFlow(): Flow<List<Announcement>> = callbackFlow {
        val subscription = firestore.collection("announcements")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val announcements = snapshot.documents.mapNotNull { it.toObject(Announcement::class.java) }
                    trySend(announcements)
                }
            }
        awaitClose { subscription.remove() }
    }

    suspend fun getAnnouncements(): List<Announcement> {
        return try {
            firestore.collection("announcements")
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    val announcement = doc.toObject(Announcement::class.java)
                    // Ensure the ID from Firestore matches the document ID if necessary
                    announcement?.copy(id = doc.id.toIntOrNull() ?: announcement.id)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun saveAnnouncement(announcement: Announcement) {
        firestore.collection("announcements")
            .document(announcement.id.toString())
            .set(announcement)
            .await()
    }

    suspend fun deleteAnnouncement(id: Int) {
        firestore.collection("announcements")
            .document(id.toString())
            .delete()
            .await()
    }

    suspend fun getTasks(userEmail: String, isAdmin: Boolean): List<Task> {
        return try {
            val collection = firestore.collection("tasks")
            val query = if (isAdmin) {
                collection
            } else {
                collection.where(
                    Filter.or(
                        Filter.equalTo("ownerEmail", userEmail),
                        Filter.equalTo("assignedTo", userEmail),
                        Filter.equalTo("assignedTo", "all")
                    )
                )
            }
            
            query.get().await().documents.mapNotNull { doc ->
                val task = doc.toObject(Task::class.java)
                task?.copy(id = doc.id.toIntOrNull() ?: task.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getTasksFlow(userEmail: String, isAdmin: Boolean): Flow<List<Task>> = callbackFlow {
        val collection = firestore.collection("tasks")
        val query = if (isAdmin) {
            collection
        } else {
            collection.where(
                Filter.or(
                    Filter.equalTo("ownerEmail", userEmail),
                    Filter.equalTo("assignedTo", userEmail),
                    Filter.equalTo("assignedTo", "all")
                )
            )
        }

        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val tasks = snapshot.documents.mapNotNull { doc ->
                    val task = doc.toObject(Task::class.java)
                    task?.copy(id = doc.id.toIntOrNull() ?: task.id)
                }
                trySend(tasks)
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun saveTask(task: Task) {
        firestore.collection("tasks")
            .document(task.id.toString())
            .set(task)
            .await()
    }

    suspend fun deleteTask(id: Int) {
        firestore.collection("tasks")
            .document(id.toString())
            .delete()
            .await()
    }
}
