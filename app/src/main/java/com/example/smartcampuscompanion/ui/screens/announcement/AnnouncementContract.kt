package com.example.smartcampuscompanion.ui.screens.announcement

import com.example.smartcampuscompanion.data.entity.Announcement

sealed class AnnouncementIntent {
    object LoadAnnouncements : AnnouncementIntent()
    data class MarkAsRead(val announcementId: Int) : AnnouncementIntent()
    object MarkAllAsRead : AnnouncementIntent()
    data class LoadAnnouncementDetail(val announcementId: Int) : AnnouncementIntent()
}

data class AnnouncementState(
    val announcements: List<Announcement> = emptyList(),
    val currentAnnouncement: Announcement? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class AnnouncementEffect {
    data class ShowError(val message: String) : AnnouncementEffect()
}
