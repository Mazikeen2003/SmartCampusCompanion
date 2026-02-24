package com.example.smartcampuscompanion.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smartcampuscompanion.data.dao.DepartmentDao
import com.example.smartcampuscompanion.data.dao.UserDao
import com.example.smartcampuscompanion.data.entity.Department
import com.example.smartcampuscompanion.data.entity.User

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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
