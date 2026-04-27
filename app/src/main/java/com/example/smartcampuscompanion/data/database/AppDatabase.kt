package com.example.smartcampuscompanion.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smartcampuscompanion.data.dao.AnnouncementDao
import com.example.smartcampuscompanion.data.dao.DepartmentDao
import com.example.smartcampuscompanion.data.dao.TaskDao
import com.example.smartcampuscompanion.data.dao.UserDao
import com.example.smartcampuscompanion.data.entity.Announcement
import com.example.smartcampuscompanion.data.entity.Department
import com.example.smartcampuscompanion.data.entity.Task
import com.example.smartcampuscompanion.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Department::class, Task::class, Announcement::class], version = 18, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun taskDao(): TaskDao
    abstract fun announcementDao(): AnnouncementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_campus_companion_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
