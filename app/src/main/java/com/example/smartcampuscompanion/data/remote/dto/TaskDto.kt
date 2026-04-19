package com.example.smartcampuscompanion.data.remote.dto

data class TaskDto(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val dueTime: String,
    val isCompleted: Boolean
)