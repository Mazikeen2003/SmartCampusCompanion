package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.entity.Announcement
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AnnouncementRepository @Inject constructor(
    private val announcementDao: AnnouncementDao,
    private val firestore: FirebaseFirestore
) {

    val allAnnouncements: Flow<List<Announcement>> =
        announcementDao.getAllAnnouncements()

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

    suspend fun insertAll(announcements: List<Announcement>) {
        announcementDao.insertAnnouncements(announcements)
    }

    suspend fun update(announcement: Announcement) {
        announcementDao.updateAnnouncement(announcement)
        try {
            firestore.collection("announcements").document(announcement.id.toString()).set(announcement).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getAnnouncementById(id: Int): Announcement? {
        return announcementDao.getAnnouncementById(id)
    }

    suspend fun markAsRead(id: Int) {
        announcementDao.markAsRead(id)
    }

    suspend fun markAllAsRead() {
        announcementDao.markAllAsRead()
    }
}
