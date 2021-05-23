package com.example.cowinnotifier.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cowinnotifier.MyApplication
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.utils.CoroutineUtil
import com.example.cowinnotifier.utils.NotificationUtil
import com.example.cowinnotifier.utils.SessionUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundSearchService() : JobService() {

    @Inject
    lateinit var apiService: APIService
    val TAG = "customtag"

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Inside onStartJob: ")

        val pincode = MyApplication.sharedPreferences.getString(AppConstants.PINCODE, "-1")
        val districtId = MyApplication.sharedPreferences.getString(AppConstants.DISTRICT_ID, "-1")

        Log.d(TAG, "Pincode: $pincode \ndistrictId: $districtId")

        if (pincode != "-1") {
            Log.d(TAG, "Pincode search")
            searchForAvailableSlots(pincode!!, AppConstants.PINCODE, params)
        } else if (districtId != "-1") {
            Log.d(TAG, "district search")
            searchForAvailableSlots(districtId!!, AppConstants.DISTRICT_ID, params)
        }
        Log.d(TAG, "returning true from startJob")

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    private fun searchForAvailableSlots(
        queryParam: String,
        queryName: String,
        params: JobParameters?
    ) {
        CoroutineUtil.io {
            Log.d(TAG, "searchForAvailableSlots: ")

            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

            val centerList: List<Center> =
                if (queryName == AppConstants.PINCODE)
                    apiService.getCalendarByPin(queryParam, currentDate).centerList
                else
                    apiService.getCalendarByDistrict(queryParam, currentDate).centerList

            Log.d(TAG, "centerList: $centerList")

            for (eachCenter in centerList) {
                val sessionList = eachCenter.sessions
                Log.d(TAG, "SessionList: $sessionList")

                for (eachSession in sessionList) {
                    if (SessionUtil.isValidSession(eachSession)) {
                        Log.d(TAG, "Inside is valid session")
                        NotificationUtil.showNotification(applicationContext, eachCenter, eachSession)
                    }
                }
            }
            jobFinished(params, false)
        }
    }
}