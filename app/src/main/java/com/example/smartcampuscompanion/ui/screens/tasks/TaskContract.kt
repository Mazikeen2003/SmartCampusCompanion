package com.example.smartcampuscompanion.ui.screens.tasks

import com.example.smartcampuscompanion.data.entity.Task

/**
 * TaskIntent: Kinakatawan ang lahat ng posibleng aksyon ng user sa Task screen.
 */
sealed class TaskIntent {
    data object LoadTasks : TaskIntent()
    data class DeleteTask(val task: Task) : TaskIntent()
    data class ToggleTaskCompletion(val task: Task) : TaskIntent()

    // Form Intents
    data class UpdateTitle(val title: String) : TaskIntent()
    data class UpdateDescription(val description: String) : TaskIntent()
    data class UpdateDate(val date: String) : TaskIntent()
    data class UpdateTime(val time: String) : TaskIntent()
    data class UpdateAssignedTo(val assignedTo: String) : TaskIntent()
    data class LoadTaskForEdit(val taskId: Int) : TaskIntent()

    data object SaveTask : TaskIntent()
    data object ClearForm : TaskIntent()
}

/**
 * TaskState: Ang "Single Source of Truth" para sa UI.
 * Ginawang Immutable para sa mas efficient na UI updates sa Compose.
 */
data class TaskState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val userRole: String = "student",

    // Form State
    val editingTaskId: Int? = null,
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val dueTime: String = "",
    val assignedTo: String = "",
    val isFormValid: Boolean = false,
    val isSaved: Boolean = false
)

/**
 * TaskEffect: Para sa mga "One-time events" na hindi bahagi ng permanenteng state.
 */
sealed class TaskEffect {
    data class ShowSnackbar(val message: String) : TaskEffect()
    data object NavigateBack : TaskEffect()
}
