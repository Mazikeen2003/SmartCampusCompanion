package com.example.smartcampuscompanion.ui.components

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.example.smartcampuscompanion.MainActivity
import com.example.smartcampuscompanion.data.repository.AnnouncementRepository
import kotlinx.coroutines.flow.drop

@Composable
fun NotificationObserver(
    repository: AnnouncementRepository,
    userRole: String
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("app_settings", Context.MODE_PRIVATE) }

    LaunchedEffect(Unit) {
        // Drop the initial list to only notify about NEW additions while the app is alive
        repository.allAnnouncements.drop(1).collect { list ->
            val latest = list.firstOrNull()
            
            // Check preferences
            val enabled = prefs.getBoolean("notifications_enabled", true)
            val silent = prefs.getBoolean("notifications_silent", false)
            
            // Only notify students, if enabled, and only if it was posted very recently (last 30 seconds)
            if (userRole == "student" && enabled && latest != null && (System.currentTimeMillis() - latest.date) < 30000) {
                showLocalNotification(context, latest.title, latest.content, silent)
            }
        }
    }
}

private fun showLocalNotification(context: Context, title: String, message: String, isSilent: Boolean) {
    val intent = Intent(context, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
    
    val pendingIntent = PendingIntent.getActivity(
        context, 0, intent,
        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
    )

    val notificationBuilder = NotificationCompat.Builder(context, "campus_updates")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setPriority(if (isSilent) NotificationCompat.PRIORITY_LOW else NotificationCompat.PRIORITY_HIGH)
        .setSilent(isSilent)
        .setContentIntent(pendingIntent)

    if (!isSilent) {
        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_ALL)
    }

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
}
