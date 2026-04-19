package com.example.smartcampuscompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.entity.Department
import com.example.smartcampuscompanion.data.repository.DepartmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
// Manages loading and error states for department data
@HiltViewModel
class DepartmentViewModel @Inject constructor(
    private val repository: DepartmentRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val departments: StateFlow<List<Department>> = repository.allDepartments
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                repository.refreshDepartments()
            } catch (e: Exception) {
                _error.value = "Failed to load departments"
            } finally {
                _loading.value = false
            }
        }
    }
}
