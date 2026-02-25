package com.example.smartcampuscompanion.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.smartcampuscompanion.data.dao.DepartmentDao
import com.example.smartcampuscompanion.data.dao.UserDao
import com.example.smartcampuscompanion.data.entity.Department
import com.example.smartcampuscompanion.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Department::class], version = 1, exportSchema = false)
@TypeConverters(com.example.smartcampuscompanion.data.database.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun departmentDao(): DepartmentDao

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
                Department("Announcements", "#81D4FA"), // Light Blue
                Department("Tasks", "#A5D6A7"), // Light Green
                Department("My Smart ID", "#CE93D8"), // Light Purple
                Department("Calendar", "#EF9A9A"), // Light Red
                Department("FAQ", "#FFCC80"), // Light Orange
                Department("Location", "#80CBC4") // Light Teal
            )
            departmentDao.insertAll(departments)
        }
    }
}
