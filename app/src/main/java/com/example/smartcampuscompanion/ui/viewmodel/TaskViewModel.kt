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

    private fun loadTasks() {
        viewModelScope.launch {
            repository.allTasks
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> 
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { tasks ->
                    _uiState.update { it.copy(tasks = tasks, isLoading = false) }
                }
        }
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
            _effect.send(TaskEffect.ShowSnackbar("Task deleted"))
        }
    }

    private fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            repository.update(task.copy(isCompleted = !task.isCompleted))
        }
    }

    private fun updateTitle(title: String) {
        _uiState.update { 
            it.copy(
                title = title,
                isFormValid = title.isNotBlank() && it.dueDate.isNotBlank() && it.dueTime.isNotBlank()
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
                isFormValid = it.title.isNotBlank() && date.isNotBlank() && it.dueTime.isNotBlank()
            ) 
        }
    }

    private fun updateTime(time: String) {
        _uiState.update { 
            it.copy(
                dueTime = time,
                isFormValid = it.title.isNotBlank() && it.dueDate.isNotBlank() && time.isNotBlank()
            ) 
        }
    }

    private fun loadTaskForEdit(taskId: Int) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId)
            task?.let { t ->
                _uiState.update {
                    it.copy(
                        editingTaskId = t.id,
                        title = t.title,
                        description = t.description,
                        dueDate = t.dueDate,
                        dueTime = t.dueTime,
                        isFormValid = true
                    )
                }
            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val task = Task(
                id = currentState.editingTaskId ?: 0,
                title = currentState.title,
                description = currentState.description,
                dueDate = currentState.dueDate,
                dueTime = currentState.dueTime
            )

            if (currentState.editingTaskId == null) {
                repository.insert(task)
            } else {
                repository.update(task)
            }
            
            _uiState.update { it.copy(isSaved = true) }
            _effect.send(TaskEffect.NavigateBack)
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
                isSaved = false
            )
        }
    }
}
