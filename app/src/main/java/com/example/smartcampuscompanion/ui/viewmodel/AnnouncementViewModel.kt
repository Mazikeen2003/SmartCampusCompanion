package com.example.smartcampuscompanion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampuscompanion.data.entity.Announcement
import com.example.smartcampuscompanion.data.repository.AnnouncementRepository
import com.example.smartcampuscompanion.ui.screens.announcement.AnnouncementIntent
import com.example.smartcampuscompanion.ui.screens.announcement.AnnouncementState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementViewModel @Inject constructor(
    private val repository: AnnouncementRepository,
) : ViewModel() {
    // Expose repository for the Notification Observer
    val repositoryForNotification = repository

    // Enables background data fetching
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

            // Dito ang diskarte: Ang UI na ang magsasabi kung Admin siya o hindi
            is AnnouncementIntent.PostAnnouncement -> postAnnouncement(intent.title, intent.content)
            is AnnouncementIntent.DeleteAnnouncement -> deleteAnnouncement(intent.announcementId)
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

    // --- ADMIN LOGIC ---

    private fun postAnnouncement(title: String, content: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Member 3: Generate a high-precision ID for notification filtering
                val timestamp = System.currentTimeMillis()
                val newAnnouncement = Announcement(
                    id = (timestamp % Int.MAX_VALUE).toInt(),
                    title = title,
                    content = content,
                    date = timestamp,
                    isRead = true
                )
                repository.insert(newAnnouncement)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage ?: "Failed to post") }
            }
        }
    }

    private fun deleteAnnouncement(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteById(id)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(error = "Delete failed") }
            }
        }
    }

    // --- STUDENT LOGIC ---

    private fun markAsRead(id: Int) {
        viewModelScope.launch { repository.markAsRead(id) }
    }

    private fun markAllAsRead() {
        viewModelScope.launch { repository.markAllAsRead() }
    }

    private fun loadAnnouncementDetail(id: Int) {
        viewModelScope.launch {
            val announcement = repository.getAnnouncementById(id)
            _uiState.update { it.copy(currentAnnouncement = announcement) }
            if (announcement != null && (!announcement.isRead)) {
                markAsRead(id)
            }
        }
    }
}