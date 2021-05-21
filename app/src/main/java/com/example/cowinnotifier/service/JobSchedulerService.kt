package com.example.cowinnotifier.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.utils.CoroutineUtil
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class JobSchedulerService : JobService() {

    @Inject
    lateinit var apiService: APIService

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("customtag", "onStartJob: ")
        val sharedPreferences =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)

        val pincode = sharedPreferences.getString(AppConstants.PINCODE, "-1")
        val districtId = sharedPreferences.getString(AppConstants.DISTRICT_ID, "-1")

        if (pincode != "-1") {
            Log.d("customtag", "Searching for pincode: $pincode")
            searchForAvailableSlots(pincode!!, params, AppConstants.PINCODE)
        } else if (districtId != "-1") {
            Log.d("customtag", "Searching for district: $districtId")
            searchForAvailableSlots(districtId!!, params, AppConstants.DISTRICT_ID)
        } else {
            jobFinished(params, false)
        }
        return true
    }

    private fun searchForAvailableSlots(
        queryParam: String,
        jobParams: JobParameters?,
        queryName: String
    ) {

        CoroutineUtil.io {
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
            jobFinished(jobParams, true)
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("customtag", "onStopJob: ")
        return true
    }
}