package com.example.cowinnotifier.utils

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants

class NotificationUtil {
    companion object {
        fun showNotification(context: Context) {
            val notification =
                NotificationCompat.Builder(context, AppConstants.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.cowin_logo)
                    .setContentTitle(AppConstants.NOTIFICATION_CHANNEL_NAME)
                    .setContentText(AppConstants.NOTIFICATION_CHANNEL_DESC)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()

            with(NotificationManagerCompat.from(context)) {
                notify(AppConstants.NOTIFICATION_ID, notification)
            }
        }
    }
}