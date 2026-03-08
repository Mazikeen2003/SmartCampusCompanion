package com.example.smartcampuscompanion.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartcampuscompanion.data.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.dao.TaskDao
import com.example.smartcampuscompanion.data.entity.Announcement
import com.example.smartcampuscompanion.data.entity.Task

@Database(entities = [Announcement::class, Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
    abstract fun taskDao(): TaskDao
}
