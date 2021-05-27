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
            val jobScheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo =
                JobInfo.Builder(
                    AppConstants.JOB_SCHEDULER_ID,
                    ComponentName(context, BackgroundSearchService::class.java)
                )

            val job = jobInfo.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(AppConstants.SERVICE_REPEAT_INTERVAL)
                .setPersisted(true)
                .build()
            jobScheduler.cancelAll()    // Canceling all previously added job if any
            jobScheduler.schedule(job)
        }

        fun isAnyJobScheduled(context: Context): Boolean {
            val jobScheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            for (eachJobInfo in jobScheduler.allPendingJobs) {
                if (eachJobInfo.id == AppConstants.JOB_SCHEDULER_ID)
                    return true
            }
            return false
        }
    }
}