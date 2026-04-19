package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.entity.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnnouncementRepository @Inject constructor(
    private val announcementDao: AnnouncementDao,
    private val firestore: FirebaseFirestore
) {

    // Sinisiguro na ang pag-fetch ng listahan ay nasa background thread
    val allAnnouncements: Flow<List<Announcement>> =
        announcementDao.getAllAnnouncements()
            .flowOn(Dispatchers.IO)
            .conflate()

    suspend fun insert(announcement: Announcement) = withContext(Dispatchers.IO) {
    suspend fun refreshAnnouncements() {
        try {
            val snapshot = firestore.collection("announcements").get().await()
            val entities = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Announcement::class.java)
            }
            announcementDao.insertAnnouncements(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insert(announcement: Announcement) {
        announcementDao.insertAnnouncement(announcement)
        try {
            firestore.collection("announcements").document(announcement.id.toString()).set(announcement).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insertAll(announcements: List<Announcement>) = withContext(Dispatchers.IO) {
        announcementDao.insertAnnouncements(announcements)
    }

    suspend fun update(announcement: Announcement) = withContext(Dispatchers.IO) {
        announcementDao.updateAnnouncement(announcement)
        try {
            firestore.collection("announcements").document(announcement.id.toString()).set(announcement).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getAnnouncementById(id: Int): Announcement? = withContext(Dispatchers.IO) {
        announcementDao.getAnnouncementById(id)
    }

    suspend fun markAsRead(id: Int) = withContext(Dispatchers.IO) {
        announcementDao.markAsRead(id)
    }

    suspend fun markAllAsRead() = withContext(Dispatchers.IO) {
        announcementDao.markAllAsRead()
    }

    // Idinagdag ito para sa Admin functionality na nasa ViewModel mo
    suspend fun deleteById(id: Int) = withContext(Dispatchers.IO) {
        val announcement = announcementDao.getAnnouncementById(id)
        announcement?.let {
            announcementDao.deleteAnnouncement(it)
        }
    }
}