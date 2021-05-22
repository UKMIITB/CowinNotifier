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

            val workRequest = PeriodicWorkRequestBuilder<BackgroundSearchService>(AppConstants.SERVICE_REPEAT_INTERVAL, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, AppConstants.SERVICE_REPEAT_INTERVAL, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context)
//                .enqueueUniquePeriodicWork("Cowin Search Work", ExistingPeriodicWorkPolicy.KEEP, workRequest)
                .enqueue(workRequest)
        }
    }
}