package com.example.cowinnotifier.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.utils.SchedulerUtil
import com.example.cowinnotifier.viewmodel.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_current_search.*

@AndroidEntryPoint
class CurrentSearchActivity : AppCompatActivity() {

    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_search)

        setupView()
    }

    private fun setupView() {
        if (SchedulerUtil.isAnyJobScheduled(applicationContext)) {
            val pincode = viewModel.getStringSharedPreferenceValue(AppConstants.PINCODE)
            val districtName = viewModel.getStringSharedPreferenceValue(AppConstants.DISTRICT_NAME)
            val stateName = viewModel.getStringSharedPreferenceValue(AppConstants.STATE_NAME)
            val vaccine = viewModel.getStringSharedPreferenceValue(AppConstants.VACCINE_LIST)
            val age = viewModel.getLongSharedPreferenceValue(AppConstants.AGE_LIMIT)

            val searchParameter =
                if (pincode.isNullOrEmpty()) "$districtName, $stateName" else "$pincode"
            val ageLimit = if (age == 0L) "None" else age.toString()
            val vaccineFilter = if (vaccine.isNullOrEmpty()) "None" else vaccine

            setDisplayData(searchParameter, ageLimit, vaccineFilter)
        } else {
            displayNoDataView()
        }
    }

    private fun displayNoDataView() {
        setDisplayData("N/A", "N/A", "N/A")
        imageview_no_data.visibility = View.VISIBLE
        textview_no_job_scheduled.visibility = View.VISIBLE
    }

    private fun setDisplayData(searchParameter: String, ageLimit: String, vaccineFilter: String) {
        val locationData = "Search Location: $searchParameter"
        val ageLimitData = "Age Limit: $ageLimit"
        val vaccineFilterData = "Vaccine Filter: $vaccineFilter"
        textview_location.text = locationData
        textview_age_limit.text = ageLimitData
        textview_vaccine_name.text = vaccineFilterData
    }
}