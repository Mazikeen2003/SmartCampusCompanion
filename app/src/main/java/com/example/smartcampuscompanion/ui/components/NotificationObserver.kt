package com.example.smartcampuscompanion.ui.components

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    LaunchedEffect(Unit) {
        // Drop the initial list to only notify about NEW additions while the app is alive
        repository.allAnnouncements.drop(1).collect { list ->
            val latest = list.firstOrNull()
            
            // Only notify students, and only if it was posted very recently (last 30 seconds)
            if (userRole == "student" && latest != null && (System.currentTimeMillis() - latest.date) < 30000) {
                showLocalNotification(context, latest.title, latest.content)
            }
        }
    }
}

private fun showLocalNotification(context: Context, title: String, message: String) {
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
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(NotificationCompat.DEFAULT_ALL) // This triggers the ring/vibrate
        .setContentIntent(pendingIntent)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
}
