package com.example.cowinnotifier.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.model.Session

class NotificationUtil {
    companion object {

        fun showNotification(context: Context, center: Center, session: Session) {
            val title = "Slots available at ${center.name}"
            val content = "Slots left: ${session.available_capacity}\n" +
                    "Date: ${session.date}\n" +
                    "Vaccine Name: ${session.vaccine}\n" +
                    "Age limit: ${session.min_age_limit}"

            showNotification(context, title, content)
        }

        fun cancelAllCurrentNotification(context: Context) {
            with(NotificationManagerCompat.from(context)) {
                cancelAll()
            }
        }

        private fun showNotification(context: Context, title: String, content: String) {
            val notification =
                NotificationCompat.Builder(context, AppConstants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.cowin_logo)
                    .setContentTitle(title)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(content))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()

            with(NotificationManagerCompat.from(context)) {
                notify(content.hashCode(), notification)
            }
        }
    }
}