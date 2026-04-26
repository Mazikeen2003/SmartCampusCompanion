package com.example.smartcampuscompanion.data.remote

import com.example.smartcampuscompanion.data.entity.Announcement
import com.example.smartcampuscompanion.data.entity.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    suspend fun getTasks(): List<Task> {
        return try {
            firestore.collection("tasks")
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    val task = doc.toObject(Task::class.java)
                    task?.copy(id = doc.id.toIntOrNull() ?: task.id)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
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
