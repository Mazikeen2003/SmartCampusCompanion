package com.example.smartcampuscompanion.data.dao

import androidx.room.*
import com.example.smartcampuscompanion.data.entity.Announcement
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM announcements ORDER BY date DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Query("SELECT * FROM announcements WHERE id = :announcementId")
    suspend fun getAnnouncementById(announcementId: Int): Announcement?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<Announcement>)

    @Update
    suspend fun updateAnnouncement(announcement: Announcement)

    @Delete
    suspend fun deleteAnnouncement(announcement: Announcement)

    @Query("UPDATE announcements SET isRead = 1 WHERE id = :announcementId")
    suspend fun markAsRead(announcementId: Int)

    @Query("UPDATE announcements SET isRead = 1")
    suspend fun markAllAsRead()

    // Extra: Delete by ID directly (Optional pero mas malinis)
    @Query("DELETE FROM announcements WHERE id = :announcementId")
    suspend fun deleteAnnouncementById(announcementId: Int)
}