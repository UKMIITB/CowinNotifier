package com.example.cowinnotifier.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cowinnotifier.R
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.repository.room.AppDatabase
import com.example.cowinnotifier.repository.room.Dao
import com.example.cowinnotifier.utils.CoroutineUtil
import com.example.cowinnotifier.viewmodel.ActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: ActivityViewModel by viewModels()

    private var appDatabase: AppDatabase? = null
    private var dao: Dao? = null

    private var stateList: ArrayList<State>? = null
    private var districtList: ArrayList<District>? = null

    private val IS_STATE_DATA_LOADED: String = "isStateDataLoaded"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        appDatabase = AppDatabase.getDatabaseInstance(this)
        dao = appDatabase?.dao()

        val sharedPref: SharedPreferences = this.getPreferences(Context.MODE_PRIVATE)
        val isStateDataLoaded = sharedPref.getBoolean(IS_STATE_DATA_LOADED, false)

        if (isStateDataLoaded) {
            loadActivityMainScreen()
        } else {
            setContentView(R.layout.splash_screen)
            loadStateListInBackground(sharedPref)
        }
    }

    private fun loadActivityMainScreen() {
        setContentView(R.layout.activity_main)
        searchButtonsClickListener()
        loadStateSpinnerData()
    }

    private fun loadStateListInBackground(sharedPref: SharedPreferences) {
        CoroutineUtil.io {

            stateList = viewModel.loadStateList() as ArrayList
            for (eachState in stateList!!) {
                dao?.insertState(eachState)
            }

            with(sharedPref.edit()) {
                putBoolean(IS_STATE_DATA_LOADED, true)
                apply()
            }

            runOnUiThread {
                loadActivityMainScreen()
            }
        }
    }

    private fun loadStateSpinnerData() {
        progressBar.visibility = View.VISIBLE
        CoroutineUtil.io {
            stateList = dao?.getAllStatesList() as ArrayList

            val adapterStateList = ArrayList<String>()
            for (eachState in stateList!!) {
                adapterStateList.add(eachState.state_name)
            }

            runOnUiThread {
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, adapterStateList)
                spinner_state?.adapter = adapter

                progressBar.visibility = View.GONE
            }
            spinner_state?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    progressBar.visibility = View.VISIBLE
                    loadDistrictSpinnerData(stateList?.get(position)?.state_id)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    fun loadDistrictSpinnerData(state_id: Long?) {
        CoroutineUtil.io {
            districtList = dao?.getAllDistrictList(state_id!!) as ArrayList

            if (districtList?.size == 0) {
                loadDistrictListInBackground(state_id)
            } else {
                val adapterDistrictList = ArrayList<String>()

                for (eachDistrict in districtList!!) {
                    adapterDistrictList.add(eachDistrict.district_name)
                }

                runOnUiThread {
                    val adapter =
                        ArrayAdapter(
                            this,
                            android.R.layout.simple_spinner_item,
                            adapterDistrictList
                        )
                    spinner_district.adapter = adapter
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun loadDistrictListInBackground(state_id: Long?) {
        CoroutineUtil.io {
            districtList = viewModel.loadDistrictList(state_id!!) as ArrayList

            for (eachDistrict in districtList!!) {
                eachDistrict.state_id = state_id
                dao?.insertDistrict(eachDistrict)
            }

            runOnUiThread {
                loadDistrictSpinnerData(state_id)
            }
        }
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
            val district_id = districtList?.get(spinner_district.selectedItemPosition)?.district_id
            startActivityFromIntent("district_id", district_id.toString())
        }
    }

    private fun startActivityFromIntent(key: String, value: String) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(key, value)
        }
        startActivity(intent)
    }
}