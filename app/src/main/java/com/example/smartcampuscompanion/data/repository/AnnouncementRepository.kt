package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.entity.Announcement
import com.example.smartcampuscompanion.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnnouncementRepository @Inject constructor(
    private val announcementDao: AnnouncementDao,
    private val apiService: ApiService
) {

    val allAnnouncements: Flow<List<Announcement>> =
        announcementDao.getAllAnnouncements()

    suspend fun refreshAnnouncements() {
        try {
            val remoteAnnouncements = apiService.getAnnouncements()
            val entities = remoteAnnouncements.map { dto ->
                Announcement(
                    id = dto.id,
                    title = dto.title,
                    content = dto.content,
                    date = dto.date,
                    isRead = false // Default for new items
                )
            }
            announcementDao.insertAnnouncements(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insert(announcement: Announcement) {
        announcementDao.insertAnnouncement(announcement)
    }

    suspend fun insertAll(announcements: List<Announcement>) {
        announcementDao.insertAnnouncements(announcements)
    }

    suspend fun update(announcement: Announcement) {
        announcementDao.updateAnnouncement(announcement)
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
