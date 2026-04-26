package com.example.smartcampuscompanion.ui.screens.settings

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsPaused
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("app_settings", Context.MODE_PRIVATE) }
    
    var notificationsEnabled by remember { 
        mutableStateOf(value = prefs.getBoolean("notifications_enabled", true)) 
    }
    var isSilentMode by remember { 
        mutableStateOf(value = prefs.getBoolean("notifications_silent", false)) 
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // Dark Mode Setting
            SettingsRow(
                title = "Dark Mode",
                subtitle = "Adjust the app's color scheme",
                icon = Icons.Default.DarkMode,
                checked = isDarkMode,
                onCheckedChange = { onThemeToggle() }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Notifications Master Toggle
            SettingsRow(
                title = "Push Notifications",
                subtitle = "Receive alerts for new announcements",
                icon = Icons.Default.Notifications,
                checked = notificationsEnabled,
                onCheckedChange = { 
                    notificationsEnabled = it
                    prefs.edit().putBoolean("notifications_enabled", it).apply()
                }
            )

            // Silent Mode Toggle (Only enabled if notifications are on)
            SettingsRow(
                title = "Silent Mode",
                subtitle = "Show notifications without sound or vibration",
                icon = if (isSilentMode) Icons.Default.NotificationsPaused else Icons.Default.NotificationsActive,
                checked = isSilentMode,
                enabled = notificationsEnabled,
                onCheckedChange = { 
                    isSilentMode = it
                    prefs.edit().putBoolean("notifications_silent", it).apply()
                }
            )
        }
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f).alpha(if (enabled) 1f else 0.5f)
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}
