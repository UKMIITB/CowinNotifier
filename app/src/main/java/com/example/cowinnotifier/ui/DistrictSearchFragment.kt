package com.example.cowinnotifier.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.utils.FilterUtil
import com.example.cowinnotifier.viewmodel.ActivityViewModel
import kotlinx.android.synthetic.main.fragment_district_search.*


class DistrictSearchFragment : Fragment() {

    private val viewModel: ActivityViewModel by activityViewModels()

    private val stateList = ArrayList<State>()
    private val districtList = ArrayList<District>()

    private lateinit var stateListAdapter: ArrayAdapter<State>
    private lateinit var districtListAdapter: ArrayAdapter<District>

    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_district_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        init()
    }

    private fun init() {
        stateListAdapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, stateList)
        districtListAdapter =
            ArrayAdapter(mContext, android.R.layout.simple_spinner_item, districtList)

        spinner_state.adapter = stateListAdapter
        spinner_district.adapter = districtListAdapter

        spinner_age_filter.adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, AppConstants.AGE_LIMIT_FILTER_ARRAY)
        spinner_vaccine_filter.adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, AppConstants.VACCINE_FILTER_ARRAY)

        setupSpinnerDataObserver()
        loadStateSpinnerData()
        setupSpinnerClickListener()
        setupSearchButtonClickListener()
    }

    private fun setupSpinnerDataObserver() {

        viewModel.observeStateList().observe(viewLifecycleOwner, { stateList ->

            progressBar.visibility = View.GONE
            this.stateList.clear()
            this.stateList.addAll(stateList)
            stateListAdapter.clear()
            stateListAdapter.addAll(stateList)
            stateListAdapter.notifyDataSetChanged()
        })

        viewModel.observeDistrictList().observe(viewLifecycleOwner, { districtList ->

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

    private fun setupSpinnerClickListener() {
        spinner_state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun setupSearchButtonClickListener() {

        button_district_search.setOnClickListener {

            val districtId = districtList[spinner_district.selectedItemPosition].district_id
            val ageFilter = FilterUtil.getAgeFilterValueFromPosition(spinner_age_filter.selectedItemPosition)
            val vaccineFilter =FilterUtil.getVaccineFilterValueFromPosition(spinner_vaccine_filter.selectedItemPosition)

            viewModel.clearSharedPreferenceData()
            viewModel.updateSharedPreferenceValue(AppConstants.DISTRICT_ID, districtId.toString())
            viewModel.updateSharedPreferenceValue(AppConstants.STATE_ID, districtList[spinner_district.selectedItemPosition].state_id.toString())
            viewModel.updateSharedPreferenceValue(AppConstants.AGE_LIMIT, ageFilter)
            viewModel.updateSharedPreferenceValue(AppConstants.VACCINE, vaccineFilter)
            viewModel.updateSharedPreferenceValue(AppConstants.DISTRICT_NAME, spinner_district.selectedItem.toString())
            viewModel.updateSharedPreferenceValue(AppConstants.STATE_NAME, spinner_state.selectedItem.toString())

            viewModel.startActivityFromIntent(AppConstants.DISTRICT_ID, districtId.toString(), mContext, ageFilter, vaccineFilter)
        }
    }
}