package com.example.smartcampuscompanion.ui.screens.tasks

import com.example.smartcampuscompanion.data.entity.Task

sealed class TaskIntent {
    object LoadTasks : TaskIntent()
    data class DeleteTask(val task: Task) : TaskIntent()
    data class ToggleTaskCompletion(val task: Task) : TaskIntent()
    
    // Form Intents
    data class UpdateTitle(val title: String) : TaskIntent()
    data class UpdateDescription(val description: String) : TaskIntent()
    data class UpdateDate(val date: String) : TaskIntent()
    data class UpdateTime(val time: String) : TaskIntent()
    data class LoadTaskForEdit(val taskId: Int) : TaskIntent()
    object SaveTask : TaskIntent()
    object ClearForm : TaskIntent()
}

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    
    // Form State
    val editingTaskId: Int? = null,
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val dueTime: String = "",
    val isFormValid: Boolean = false,
    val isSaved: Boolean = false
)

sealed class TaskEffect {
    data class ShowSnackbar(val message: String) : TaskEffect()
    object NavigateBack : TaskEffect()
}
