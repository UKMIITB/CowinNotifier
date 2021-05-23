package com.example.cowinnotifier.utils

import android.content.Context
import androidx.work.*
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.service.BackgroundSearchService
import java.util.concurrent.TimeUnit

class SchedulerUtil {

    companion object {
        fun scheduleNewWork(context: Context) {

            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val workRequest = PeriodicWorkRequestBuilder<BackgroundSearchService>(
                AppConstants.SERVICE_REPEAT_INTERVAL,
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    AppConstants.SERVICE_REPEAT_INTERVAL,
                    TimeUnit.MINUTES
                )
                .build()

            WorkManager.getInstance(context)
                .enqueue(workRequest)
//                .enqueueUniquePeriodicWork(AppConstants.UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
        }
    }
}