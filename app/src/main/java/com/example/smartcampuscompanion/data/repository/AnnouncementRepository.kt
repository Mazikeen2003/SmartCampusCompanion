package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.entity.Announcement
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnnouncementRepository @Inject constructor(
    private val announcementDao: AnnouncementDao
) {

    val allAnnouncements: Flow<List<Announcement>> =
        announcementDao.getAllAnnouncements()

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