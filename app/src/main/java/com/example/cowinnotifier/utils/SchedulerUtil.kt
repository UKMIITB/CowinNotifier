package com.example.cowinnotifier.utils

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.service.BackgroundSearchService

class SchedulerUtil {

    companion object {

        fun scheduleNewWork(context: Context) {
            val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo = JobInfo.Builder(100, ComponentName(context, BackgroundSearchService::class.java))

            val job = jobInfo.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(AppConstants.SERVICE_REPEAT_INTERVAL)
                .setPersisted(true)
                .build()

            jobScheduler.schedule(job)
        }

//        fun scheduleNewWork(context: Context) {
//
//            val constraints =
//                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
//
//            val workRequest = PeriodicWorkRequestBuilder<BackgroundSearchService>(
//                AppConstants.SERVICE_REPEAT_INTERVAL,
//                TimeUnit.MINUTES
//            )
//                .setConstraints(constraints)
//                .setBackoffCriteria(
//                    BackoffPolicy.LINEAR,
//                    AppConstants.SERVICE_REPEAT_INTERVAL,
//                    TimeUnit.MINUTES
//                )
//                .build()
//
//            WorkManager.getInstance(context)
//                .enqueue(workRequest)
////                .enqueueUniquePeriodicWork(AppConstants.UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest)
//        }
    }
}