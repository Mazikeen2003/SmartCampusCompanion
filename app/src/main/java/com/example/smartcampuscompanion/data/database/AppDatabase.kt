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

@Database(entities = [User::class, Department::class, Task::class, Announcement::class], version = 4, exportSchema = false)
@TypeConverters(com.example.smartcampuscompanion.data.database.TypeConverters::class)
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
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Using a new scope to ensure the database is initialized before prepopulating
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    prepopulate(database.departmentDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun prepopulate(departmentDao: DepartmentDao) {
            val departments = listOf(
                Department("College of Engineering", 0xFFE3F2FD),
                Department("School of Business", 0xFFF1F8E9),
                Department("Information Technology", 0xFFFFF3E0),
                Department("Architecture & Design", 0xFFF3E5F5),
                Department("College of Arts & Sciences", 0xFFE0F2F1),
                Department("School of Education", 0xFFFFF9C4)
            )
            departmentDao.insertAll(departments)
        }
    }
}
