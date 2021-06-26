package com.example.cowinnotifier.service

import android.app.job.JobParameters
import android.app.job.JobService
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

    override fun onStartJob(params: JobParameters?): Boolean {

//        val pincode = MyApplication.sharedPreferences.getString(AppConstants.PINCODE, "-1")
//        val districtId = MyApplication.sharedPreferences.getString(AppConstants.DISTRICT_ID, "-1")
//        val ageLimit = MyApplication.sharedPreferences.getLong(AppConstants.AGE_LIMIT, 0)
//        val vaccineFilter = MyApplication.sharedPreferences.getString(AppConstants.VACCINE_LIST, "")
//
//
//        if (pincode != "-1") {
//            searchForAvailableSlots(
//                pincode!!,
//                AppConstants.PINCODE,
//                ageLimit,
//                vaccineFilter!!,
//                params
//            )
//        } else if (districtId != "-1") {
//            searchForAvailableSlots(
//                districtId!!,
//                AppConstants.DISTRICT_ID,
//                ageLimit,
//                vaccineFilter!!,
//                params
//            )
//        }

        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

//    private fun searchForAvailableSlots(
//        queryParam: String,
//        queryName: String,
//        ageLimit: Long,
//        vaccineFilter: String,
//        params: JobParameters?
//    ) {
//        CoroutineUtil.io {
//
//            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
//
//
//            val centerList: List<Center> =
//                if (queryName == AppConstants.PINCODE)
//                    apiService.getCalendarByPin(queryParam, currentDate).centerList
//                else
//                    apiService.getCalendarByDistrict(queryParam, currentDate).centerList
//
//            var isNotificationCancelAllRequired = true
//
//            for (eachCenter in centerList) {
//                val sessionList = eachCenter.sessions
//
//                for (eachSession in sessionList) {
//                    if (SessionUtil.isValidSessionForNotificationPush(
//                            eachSession,
//                            ageLimit,
//                            vaccineFilter
//                        )
//                    ) {
//
//                        if (isNotificationCancelAllRequired) {
//                            NotificationUtil.cancelAllCurrentNotification(applicationContext)
//                            isNotificationCancelAllRequired = false
//                        }
//                        NotificationUtil.showNotification(
//                            applicationContext,
//                            eachCenter,
//                            eachSession
//                        )
//                    }
//                }
//            }
//            jobFinished(params, false)
//        }
//    }
}