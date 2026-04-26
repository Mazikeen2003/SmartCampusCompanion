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

@Database(entities = [User::class, Department::class, Task::class, Announcement::class], version = 13, exportSchema = false)
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
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateData(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateData(db: AppDatabase) {
            val departments = listOf(
                Department("College of Engineering", 0xFFFF0000, "Dean's Office: Room 301\nAvailable Courses: Civil, Mechanical, Electrical Engineering."), // Red
                Department("School of Business", 0xFFFFFF00, "Location: Business Center\nAccredited by AACSB. Focus on Entrepreneurship."), // Yellow
                Department("Information Technology", 0xFF008080, "Labs: 4th Floor Tech Hub\nSpecializations: Cybersecurity, Data Science, AI."), // Teal
                Department("Architecture & Design", 0xFFFFA500, "Studio: Arts Wing\nFocus on Sustainable Design and Urban Planning."), // Orange
                Department("College of Arts & Sciences", 0xFF800000, "Office: Humanities Hall\nDepartments: Psychology, Biology, Literature."), // Maroon
                Department("School of Education", 0xFF0000FF, "Training: Lab School\nPrograms: Elementary and Secondary Education.") // Blue
            )
            db.departmentDao().insertAll(departments)

            val announcements = listOf(
                Announcement(
                    title = "Midterm Examination Schedule",
                    content = "The official midterm examination schedule for Fall 2024 has been posted. Please check the student portal for your specific slots.",
                    date = System.currentTimeMillis() - 86400000 
                ),
                Announcement(
                    title = "Campus Maintenance Notice",
                    content = "The Main Library will be closed for maintenance this Sunday, Oct 27, from 8:00 AM to 5:00 PM.",
                    date = System.currentTimeMillis() - 172800000
                ),
                Announcement(
                    title = "Smart Campus Hackathon 2024",
                    content = "Join us for a 48-hour hackathon to build solutions for a smarter campus! Register now at the IT building office.",
                    date = System.currentTimeMillis() - 43200000
                )
            )
            announcements.forEach { db.announcementDao().insertAnnouncement(it) }

            val tasks = listOf(
                Task(title = "Submit Lab Report", description = "Physics Lab 3 report on Thermodynamics", dueDate = "25/10/2024", dueTime = "23:59"),
                Task(title = "Buy Textbooks", description = "Calculus and Economics books", dueDate = "26/10/2024", dueTime = "10:00"),
                Task(title = "Group Meeting", description = "Discuss project milestones", dueDate = "27/10/2024", dueTime = "14:30")
            )
            tasks.forEach { db.taskDao().insertTask(it) }
        }
    }
}
