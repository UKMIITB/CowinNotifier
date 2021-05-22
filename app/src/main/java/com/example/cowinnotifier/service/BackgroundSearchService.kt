package com.example.cowinnotifier.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.utils.SchedulerUtil
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class BackgroundSearchService(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var apiService: APIService

    override suspend fun doWork(): Result {
        Log.d("customtag", "ondoWork: ")
        val sharedPreferences =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val pincode = sharedPreferences.getString(AppConstants.PINCODE, "-1")
        val districtId = sharedPreferences.getString(AppConstants.DISTRICT_ID, "-1")
        Log.d("customtag", "pincode: $pincode \n districtID: $districtId")
        if (pincode != "-1") {
            Log.d("customtag", "Searching for pincode: $pincode")
            searchForAvailableSlots(pincode!!, AppConstants.PINCODE)
        } else if (districtId != "-1") {
            Log.d("customtag", "Searching for district: $districtId")
            searchForAvailableSlots(districtId!!, AppConstants.DISTRICT_ID)
        }
        return Result.success()
    }

    private suspend fun searchForAvailableSlots(queryParam: String, queryName: String) {

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        val centerList: List<Center>
        if (queryName == AppConstants.PINCODE) {
            centerList = apiService.getCalendarByPin(queryParam, currentDate).centerList
        } else {
            centerList = apiService.getCalendarByDistrict(queryParam, currentDate).centerList
        }

        for (eachCenter in centerList) {
            val sessionList = eachCenter.sessions

            for (eachSession in sessionList) {
                Log.d("customtag", eachSession.toString())
                if (eachSession.available_capacity > 0 && eachSession.date == currentDate) {
                    Log.d("customtag", "Hey I got a slot: $eachSession")
                }
            }
        }
    }
}