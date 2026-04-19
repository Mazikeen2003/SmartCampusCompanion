package com.example.smartcampuscompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.entity.Task
import com.example.smartcampuscompanion.data.repository.TaskRepository
import com.example.smartcampuscompanion.ui.screens.tasks.TaskEffect
import com.example.smartcampuscompanion.ui.screens.tasks.TaskIntent
import com.example.smartcampuscompanion.ui.screens.tasks.TaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
// Fetches remote data on initialization
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskState())
    val uiState: StateFlow<TaskState> = _uiState.asStateFlow()

    private val _effect = Channel<TaskEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(TaskIntent.LoadTasks)
        refresh()
    }

    fun handleIntent(intent: TaskIntent) {
        when (intent) {
            is TaskIntent.LoadTasks -> loadTasks()
            is TaskIntent.DeleteTask -> deleteTask(intent.task)
            is TaskIntent.ToggleTaskCompletion -> toggleTaskCompletion(intent.task)
            is TaskIntent.UpdateTitle -> updateTitle(intent.title)
            is TaskIntent.UpdateDescription -> updateDescription(intent.description)
            is TaskIntent.UpdateDate -> updateDate(intent.date)
            is TaskIntent.UpdateTime -> updateTime(intent.time)
            is TaskIntent.LoadTaskForEdit -> loadTaskForEdit(intent.taskId)
            is TaskIntent.SaveTask -> saveTask()
            is TaskIntent.ClearForm -> clearForm()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            repository.refreshTasks()
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.allTasks
                .onStart { _uiState.update { it.copy(isLoading = true, error = null) } }
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
                .collect { tasks ->
                    _uiState.update { it.copy(tasks = tasks, isLoading = false) }
                }
        }
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.delete(task)
                _effect.send(TaskEffect.ShowSnackbar("Task deleted successfully"))
            } catch (e: Exception) {
                _effect.send(TaskEffect.ShowSnackbar("Failed to delete task"))
            }
        }
    }

    private fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            try {
                repository.update(task.copy(isCompleted = !task.isCompleted))
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Could not update task status") }
            }
        }
    }

    // Helper function para sa validation para "Clean" tignan
    private fun validateForm(title: String, date: String, time: String): Boolean {
        return title.isNotBlank() && date.isNotBlank() && time.isNotBlank()
    }

    private fun updateTitle(title: String) {
        _uiState.update {
            it.copy(
                title = title,
                isFormValid = validateForm(title, it.dueDate, it.dueTime)
            )
        }
    }

    private fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    private fun updateDate(date: String) {
        _uiState.update {
            it.copy(
                dueDate = date,
                isFormValid = validateForm(it.title, date, it.dueTime)
            )
        }
    }

    private fun updateTime(time: String) {
        _uiState.update {
            it.copy(
                dueTime = time,
                isFormValid = validateForm(it.title, it.dueDate, time)
            )
        }
    }

    private fun loadTaskForEdit(taskId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val task = repository.getTaskById(taskId)
            _uiState.update { state ->
                if (task != null) {
                    state.copy(
                        editingTaskId = task.id,
                        title = task.title,
                        description = task.description,
                        dueDate = task.dueDate,
                        dueTime = task.dueTime,
                        isFormValid = true,
                        isLoading = false
                    )
                } else {
                    state.copy(isLoading = false, error = "Task not found")
                }
            }
        }
    }

    private fun saveTask() {
        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val currentState = _uiState.value
                val task = Task(
                    id = currentState.editingTaskId ?: 0,
                    title = currentState.title,
                    description = currentState.description,
                    dueDate = currentState.dueDate,
                    dueTime = currentState.dueTime,
                    isCompleted = false // Default or retain existing state if editing
                )

                if (currentState.editingTaskId == null) {
                    repository.insert(task)
                } else {
                    repository.update(task)
                }

                _uiState.update { it.copy(isSaved = true, isLoading = false) }
                _effect.send(TaskEffect.NavigateBack)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to save task") }
            }
        }
    }

    private fun clearForm() {
        _uiState.update {
            it.copy(
                editingTaskId = null,
                title = "",
                description = "",
                dueDate = "",
                dueTime = "",
                isFormValid = false,
                isSaved = false,
                error = null
            )
        }
    }
}