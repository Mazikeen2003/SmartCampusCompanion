package com.example.smartcampuscompanion.ui.screens.announcement

import com.example.smartcampuscompanion.data.entity.Announcement

sealed class AnnouncementIntent {
    // Student & Common Intents
    data object LoadAnnouncements : AnnouncementIntent()
    data class MarkAsRead(val announcementId: Int) : AnnouncementIntent()
    data object MarkAllAsRead : AnnouncementIntent()
    data class LoadAnnouncementDetail(val announcementId: Int) : AnnouncementIntent()

    // Admin Intents (Idinagdag para sa rubric mo)
    data class PostAnnouncement(val title: String, val content: String) : AnnouncementIntent()
    data class DeleteAnnouncement(val announcementId: Int) : AnnouncementIntent()
}

data class AnnouncementState(
    val announcements: List<Announcement> = emptyList(),
    val currentAnnouncement: Announcement? = null,
    val isLoading: Boolean = false,
    val error: String? = null,

    // Role state (Optional pero helpful para sa logic check sa UI)
    val isAdmin: Boolean = false
)

sealed class AnnouncementEffect {
    data class ShowError(val message: String) : AnnouncementEffect()
    data class ShowSnackbar(val message: String) : AnnouncementEffect()
    data object NavigateBack : AnnouncementEffect() // Useful pagkatapos mag-post ng admin
}