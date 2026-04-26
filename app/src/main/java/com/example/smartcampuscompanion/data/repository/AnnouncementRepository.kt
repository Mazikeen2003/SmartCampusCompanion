package com.example.smartcampuscompanion.data.repository

import com.example.smartcampuscompanion.data.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.entity.Announcement
import com.example.smartcampuscompanion.data.remote.FirebaseDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnnouncementRepository @Inject constructor(
    private val announcementDao: AnnouncementDao,
    private val remoteDataSource: FirebaseDataSource
) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        // Start listening to Firestore real-time updates immediately
        repositoryScope.launch {
            remoteDataSource.getAnnouncementsFlow().collect { remoteList ->
                announcementDao.insertAnnouncements(remoteList)
            }
        }
    }

    val allAnnouncements: Flow<List<Announcement>> =
        announcementDao.getAllAnnouncements()
            .flowOn(Dispatchers.IO)
            .conflate()

    suspend fun refreshAnnouncements() {
        try {
            val entities = remoteDataSource.getAnnouncements()
            announcementDao.insertAnnouncements(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insert(announcement: Announcement) = withContext(Dispatchers.IO) {
        val id = announcementDao.insertAnnouncement(announcement)
        try {
            // Ensure the entity sent to Firestore has the Room-generated ID
            remoteDataSource.saveAnnouncement(announcement.copy(id = id.toInt()))
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
            remoteDataSource.saveAnnouncement(announcement)
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


    suspend fun deleteById(id: Int) = withContext(Dispatchers.IO) {
        val announcement = announcementDao.getAnnouncementById(id)
        announcement?.let {
            announcementDao.deleteAnnouncement(it)
            try {
                remoteDataSource.deleteAnnouncement(it.id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
