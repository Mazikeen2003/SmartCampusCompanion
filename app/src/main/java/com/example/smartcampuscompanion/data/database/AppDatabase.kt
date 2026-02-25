package com.example.smartcampuscompanion.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smartcampuscompanion.data.dao.DepartmentDao
import com.example.smartcampuscompanion.data.dao.UserDao
import com.example.smartcampuscompanion.data.dao.TaskDao
import com.example.smartcampuscompanion.data.entity.Department
import com.example.smartcampuscompanion.data.entity.User
import com.example.smartcampuscompanion.data.entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Department::class, Task::class], version = 2, exportSchema = false)
@TypeConverters(com.example.smartcampuscompanion.data.database.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun taskDao(): TaskDao

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
                Department("Announcements", 0xFF81D4FA), // Light Blue
                Department("Tasks", 0xFFA5D6A7), // Light Green
                Department("My Smart ID", 0xFFCE93D8), // Light Purple
                Department("Calendar", 0xFFEF9A9A), // Light Red
                Department("FAQ", 0xFFFFCC80), // Light Orange
                Department("Location", 0xFF80CBC4) // Light Teal
            )
            departmentDao.insertAll(departments)
        }
    }
}
