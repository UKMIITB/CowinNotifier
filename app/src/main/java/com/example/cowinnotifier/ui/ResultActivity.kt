package com.example.cowinnotifier.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.ui.adapters.CenterAdapter
import com.example.cowinnotifier.utils.SchedulerUtil
import com.example.cowinnotifier.viewmodel.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_result.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {

    private val viewModel: ActivityViewModel by viewModels()

    private val centerList = ArrayList<Center>()
    private lateinit var centerAdapter: CenterAdapter

    private var ageLimit: Long = 0L
    private lateinit var vaccineList: ArrayList<String>
    private lateinit var dose: String
    private lateinit var currentDate: String

    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        bundle = intent.getBundleExtra(AppConstants.SEARCH_DATA)!!
        init()

        if (bundle.containsKey(AppConstants.SEARCH_BY_DISTRICT)) {
            startDistrictWiseSearch()
        } else if (bundle.containsKey(AppConstants.SEARCH_BY_PINCODE)) {
            startPincodeWiseSearch()
        }
    }

    private fun init() {
        ageLimit = bundle.getLong(AppConstants.AGE_LIMIT, 0L)
        vaccineList =
            bundle.getStringArrayList(AppConstants.VACCINE_LIST) as ArrayList<String>
        dose = bundle.getString(AppConstants.DOSE, "")
        currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        val layoutManager = LinearLayoutManager(this)
        centerAdapter = CenterAdapter(centerList, bundle)

        recyclerview_center_list.layoutManager = layoutManager
        recyclerview_center_list.adapter = centerAdapter

        viewModel.observeCenterList().observe(this, {
            centerAdapter.updateAdapterData(it)
            updateProgressBar(View.GONE)
            if (it.isNullOrEmpty()) {
                imageview_no_data.visibility = View.VISIBLE
                textview_no_data_message.visibility = View.VISIBLE
            }
        })
    }

    private fun startPincodeWiseSearch() {
        updateProgressBar(View.VISIBLE)
        val pincode = bundle.getString(AppConstants.SEARCH_BY_PINCODE, "")
        viewModel.loadCalendarByPincode(pincode, ageLimit, vaccineList, dose, currentDate)
    }

    private fun startDistrictWiseSearch() {
        updateProgressBar(View.VISIBLE)
        val district_id = bundle.getString(AppConstants.SEARCH_BY_DISTRICT, "")
        viewModel.loadCalendarByDistrict(district_id, ageLimit, vaccineList, dose, currentDate)
    }

    private fun updateProgressBar(visibility: Int) {
        progressBar.visibility = visibility
    }

    override fun onBackPressed() {
        if (imageview_no_data.visibility == View.VISIBLE) {
            super.onBackPressed()
        } else {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage(AppConstants.ALERT_DIALOG_MESSAGE)
            alertDialog.setTitle(AppConstants.ALERT_DIALOG_TITLE)
            alertDialog.setPositiveButton("Yes") { _, _ ->
                SchedulerUtil.scheduleNewWork(applicationContext)
                super.onBackPressed()
            }

            alertDialog.setNegativeButton("No") { _, _ ->
                super.onBackPressed()
            }

            alertDialog.setCancelable(true)
            alertDialog.create().show()
        }
    }
}