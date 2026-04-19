package com.example.smartcampuscompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.repository.AnnouncementRepository
import com.example.smartcampuscompanion.ui.screens.announcement.AnnouncementIntent
import com.example.smartcampuscompanion.ui.screens.announcement.AnnouncementState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val repository: AnnouncementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnnouncementState())
    val uiState: StateFlow<AnnouncementState> = _uiState.asStateFlow()

    init {
        handleIntent(AnnouncementIntent.LoadAnnouncements)
        refresh()
    }

    fun handleIntent(intent: AnnouncementIntent) {
        when (intent) {
            is AnnouncementIntent.LoadAnnouncements -> loadAnnouncements()
            is AnnouncementIntent.MarkAsRead -> markAsRead(intent.announcementId)
            is AnnouncementIntent.MarkAllAsRead -> markAllAsRead()
            is AnnouncementIntent.LoadAnnouncementDetail -> loadAnnouncementDetail(intent.announcementId)
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            repository.refreshAnnouncements()
        }
    }

    private fun loadAnnouncements() {
        viewModelScope.launch {
            repository.allAnnouncements
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { list ->
                    _uiState.update { it.copy(announcements = list, isLoading = false) }
                }
        }
    }

    private fun markAsRead(id: Int) {
        viewModelScope.launch {
            repository.markAsRead(id)
        }
    }

    private fun markAllAsRead() {
        viewModelScope.launch {
            repository.markAllAsRead()
        }
    }

    private fun loadAnnouncementDetail(id: Int) {
        viewModelScope.launch {
            val announcement = repository.getAnnouncementById(id)
            _uiState.update { it.copy(currentAnnouncement = announcement) }
            if (announcement != null && !announcement.isRead) {
                markAsRead(id)
            }
        }
    }
}
