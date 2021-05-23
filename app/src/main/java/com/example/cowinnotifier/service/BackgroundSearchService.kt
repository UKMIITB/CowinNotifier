package com.example.cowinnotifier.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cowinnotifier.MyApplication
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.utils.NotificationUtil
import com.example.cowinnotifier.utils.SessionUtil
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class BackgroundSearchService(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var apiService: APIService
    val TAG = "customtag"

    override suspend fun doWork(): Result {
        Log.d(TAG, "Inside doWork: ")

        val pincode = MyApplication.sharedPreferences.getString(AppConstants.PINCODE, "-1")
        val districtId = MyApplication.sharedPreferences.getString(AppConstants.DISTRICT_ID, "-1")

        Log.d(TAG, "Pincode: $pincode \ndistrictId: $districtId")

        if (pincode != "-1") {
            Log.d(TAG, "Pincode search")
            searchForAvailableSlots(pincode!!, AppConstants.PINCODE)
        } else if (districtId != "-1") {
            Log.d(TAG, "district search")
            searchForAvailableSlots(districtId!!, AppConstants.DISTRICT_ID)
        }
        Log.d(TAG, "returning success from dowWork")
        return Result.success()
    }

    private suspend fun searchForAvailableSlots(queryParam: String, queryName: String) {
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
    }
}