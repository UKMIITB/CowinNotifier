package com.example.cowinnotifier.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowinnotifier.R
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.ui.adapters.CenterAdapter
import com.example.cowinnotifier.utils.CoroutineUtil
import com.example.cowinnotifier.viewmodel.ActivityViewModel
import kotlinx.android.synthetic.main.activity_result.*
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {

    private val viewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val intent = intent
        if (intent.hasExtra("district_id")) {
            startDistrictWiseSearch(intent)
        } else if (intent.hasExtra("pincode")) {
//            startPincodeWiseSearch(intent)
        }
    }

    private fun updateProgressBar(visibility: Int) {
        progressBar.visibility = visibility
    }

    private fun startDistrictWiseSearch(intent: Intent) {
        updateProgressBar(View.VISIBLE)
        val district_id = intent.getStringExtra("district_id")
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        CoroutineUtil.io {
            val centerList = viewModel.getCalendarByDistrictList(district_id!!, currentDate)

            runOnUiThread {
                setUpCenterAdapter(centerList)
            }
        }
    }

    private fun setUpCenterAdapter(centerList: List<Center>) {
        updateProgressBar(View.GONE)

        val layoutManager = LinearLayoutManager(this)
        val centerAdapter = CenterAdapter(centerList)

        recyclerview_center_list.layoutManager = layoutManager
        recyclerview_center_list.adapter = centerAdapter
    }
}