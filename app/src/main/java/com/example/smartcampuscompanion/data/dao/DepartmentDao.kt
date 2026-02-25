package com.example.smartcampuscompanion.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartcampuscompanion.data.entity.Department
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(departments: List<Department>)

    @Query("SELECT * FROM departments")
    fun getAll(): Flow<List<Department>>
}
