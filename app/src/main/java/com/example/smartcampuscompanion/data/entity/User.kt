package com.example.smartcampuscompanion.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val email: String = "",
    val password: String = "",
    val role: String = "student"
)
