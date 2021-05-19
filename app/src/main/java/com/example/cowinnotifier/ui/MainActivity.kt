package com.example.cowinnotifier.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cowinnotifier.R
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.repository.room.AppDatabase
import com.example.cowinnotifier.repository.room.Dao
import com.example.cowinnotifier.viewmodel.ActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ActivityViewModel

    private lateinit var appDatabase: AppDatabase
    private lateinit var dao: Dao
    private val stateList = ArrayList<State>()
    private val districtList = ArrayList<District>()

    private val stateListAdapter =
        ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList)
    private val districtListAdapter =
        ArrayAdapter(this, android.R.layout.simple_spinner_item, districtList)

    private val IS_STATE_DATA_LOADED: String = "isStateDataLoaded"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this).get(ActivityViewModel::class.java)
        //TODO -> pass dao to viewmodel
        appDatabase = AppDatabase.getDatabaseInstance(this)!!
        dao = appDatabase.dao()

        val sharedPref: SharedPreferences = this.getPreferences(Context.MODE_PRIVATE)
        val isStateDataLoaded = sharedPref.getBoolean(IS_STATE_DATA_LOADED, false)

        if (isStateDataLoaded) {
            loadActivityMainScreen()
        } else {
            viewModel.addStateListInDB()

            with(sharedPref.edit()) {
                putBoolean(IS_STATE_DATA_LOADED, true)
                apply()
            }

            loadActivityMainScreen()
        }
    }

    private fun loadActivityMainScreen() {
        setContentView(R.layout.activity_main)

        spinner_state?.adapter = stateListAdapter
        spinner_district.adapter = districtListAdapter

        spinner_state?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                loadDistrictSpinnerData(stateList[position].state_id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        searchButtonsClickListener()
        loadStateSpinnerData()
    }

    private fun loadStateSpinnerData() {
        progressBar.visibility = View.VISIBLE

        viewModel.getStateListFromDB().observe(this, { stateList ->

            this.stateList.clear()
            this.stateList.addAll(stateList)
            stateListAdapter.addAll(stateList)
            stateListAdapter.notifyDataSetChanged()

            progressBar.visibility = View.GONE
        })
    }

    fun loadDistrictSpinnerData(state_id: Long?) {
        progressBar.visibility = View.VISIBLE

        viewModel.getDistrictListFromDB(state_id!!).observe(this, { districtList ->

            if (districtList.isEmpty()) {
                loadDistrictListInBackground(state_id)
            } else {

                this.districtList.clear()
                this.districtList.addAll(districtList)
                districtListAdapter.addAll(districtList)
                districtListAdapter.notifyDataSetChanged()

                progressBar.visibility = View.GONE
            }
        })
    }

    private fun loadDistrictListInBackground(state_id: Long?) {
        viewModel.addDistrictListInDB(state_id!!)
        loadDistrictSpinnerData(state_id)
    }

    private fun searchButtonsClickListener() {
        button_pincode_search.setOnClickListener {
            val pinEntered = edit_text_pincode.text.toString()

            if (pinEntered.length != 6) {
                Toast.makeText(this, "Please enter a valid pincode", Toast.LENGTH_SHORT).show()
            } else {
                startActivityFromIntent("pincode", pinEntered)
            }
        }

        button_district_search.setOnClickListener {
            val districtId = districtList[spinner_district.selectedItemPosition].district_id
            startActivityFromIntent("district_id", districtId.toString())
        }
    }

    private fun startActivityFromIntent(key: String, value: String) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(key, value)
        }
        startActivity(intent)
    }
}