package com.example.smartcampuscompanion.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "departments")
data class Department(
    @PrimaryKey
    val name: String = "",
    val bgColor: Long = 0,
    val description: String = ""
)
