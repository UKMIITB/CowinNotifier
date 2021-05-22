package com.example.cowinnotifier.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowinnotifier.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        init()

        if (intent.hasExtra("district_id")) {
            startDistrictWiseSearch(intent)
        } else if (intent.hasExtra("pincode")) {
            startPincodeWiseSearch(intent)
        }
    }

    private fun init() {
        val layoutManager = LinearLayoutManager(this)
        centerAdapter = CenterAdapter(centerList)

        recyclerview_center_list.layoutManager = layoutManager
        recyclerview_center_list.adapter = centerAdapter

        viewModel.observeCenterList().observe(this, {
            centerAdapter.updateAdapterData(it)
            updateProgressBar(View.GONE)
        })
    }

    private fun startPincodeWiseSearch(intent: Intent) {
        updateProgressBar(View.VISIBLE)
        val pincode = intent.getStringExtra("pincode")
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        viewModel.loadCalendarByPincode(pincode!!, currentDate)
    }

    private fun startDistrictWiseSearch(intent: Intent) {
        updateProgressBar(View.VISIBLE)
        val district_id = intent.getStringExtra("district_id")
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        viewModel.loadCalendarByDistrict(district_id!!, currentDate)
    }

    private fun updateProgressBar(visibility: Int) {
        progressBar.visibility = visibility
    }

    override fun onBackPressed() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("Do you want to continue search in background and send notification when slots are available")
        alertDialog.setTitle("Continue searching in background")
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