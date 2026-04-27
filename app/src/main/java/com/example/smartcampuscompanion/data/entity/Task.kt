package com.example.smartcampuscompanion.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    indices = [Index(value = ["title", "ownerEmail"], unique = true)]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val dueTime: String = "",
    val isCompleted: Boolean = false,
    val ownerEmail: String = "", // Who created the task
    val assignedTo: String = "", // "all", specific email, or same as owner
    val createdByAdmin: Boolean = false // If true, students can only toggle completion
)
