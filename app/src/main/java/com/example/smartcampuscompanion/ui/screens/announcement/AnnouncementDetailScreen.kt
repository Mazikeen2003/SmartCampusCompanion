package com.example.smartcampuscompanion.ui.screens.announcement

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartcampuscompanion.data.entity.Announcement
import com.example.smartcampuscompanion.ui.viewmodel.AnnouncementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementDetailScreen(
    announcementId: Int,
    viewModel: AnnouncementViewModel,
    onBackClick: () -> Unit
) {

    var announcement by remember { mutableStateOf<Announcement?>(null) }

    LaunchedEffect(announcementId) {
        val result = viewModel.getAnnouncementById(announcementId)
        announcement = result

        result?.let {
            if (!it.isRead) {
                viewModel.markAsRead(it.id)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Announcement") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            announcement?.let {

                Text(
                    text = it.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = it.content,
                    style = MaterialTheme.typography.bodyMedium
                )

            } ?: run {
                Text("Loading announcement...")
            }

        }
    }
}
