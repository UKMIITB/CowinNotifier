package com.example.cowinnotifier.ui

import android.content.Intent
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
import com.example.cowinnotifier.viewmodel.ActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: ActivityViewModel by viewModels()

    private val stateList = ArrayList<State>()
    private val districtList = ArrayList<District>()

    private lateinit var stateListAdapter: ArrayAdapter<State>
    private lateinit var districtListAdapter: ArrayAdapter<District>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        stateListAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList)
        districtListAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districtList)

        spinner_state?.adapter = stateListAdapter
        spinner_district.adapter = districtListAdapter

        setupSpinnerDataObserver()
        loadStateSpinnerData()
        setupSpinnerClickListener()
        setupSearchButtonClickListener()
    }

    private fun setupSpinnerClickListener() {
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
    }


    private fun setupSpinnerDataObserver() {

        viewModel.observeStateList().observe(this, { stateList ->
            progressBar.visibility = View.GONE
            this.stateList.clear()
            this.stateList.addAll(stateList)
            stateListAdapter.clear()
            stateListAdapter.addAll(stateList)
            stateListAdapter.notifyDataSetChanged()
        })

        viewModel.observeDistrictList().observe(this, { districtList ->
            progressBar.visibility = View.GONE
            this.districtList.clear()
            this.districtList.addAll(districtList)
            districtListAdapter.clear()
            districtListAdapter.addAll(districtList)
            districtListAdapter.notifyDataSetChanged()
        })
    }

    private fun loadStateSpinnerData() {
        progressBar.visibility = View.VISIBLE
        viewModel.loadStateListData()
    }

    private fun loadDistrictSpinnerData(state_id: Long) {
        progressBar.visibility = View.VISIBLE
        viewModel.loadDistrictListData(state_id)
    }

    private fun setupSearchButtonClickListener() {
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
