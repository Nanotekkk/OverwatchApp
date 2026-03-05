package com.example.overwatchapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            showNotification(it.title ?: "Notification", it.body ?: "")
        }
    }

    private fun showNotification(title: String, body: String) {

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "daily_channel",
                "Daily Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, "daily_channel")
            .setContentTitle(title)
            .setContentText(body)
            .build()

        manager.notify(1, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        println("FCM_TOKEN: $token")

        // Ici tu peux envoyer le token à ton serveur si besoin
    }
}