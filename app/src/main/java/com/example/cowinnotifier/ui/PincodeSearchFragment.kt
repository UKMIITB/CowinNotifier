package com.example.cowinnotifier.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cowinnotifier.R
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.utils.FilterUtil
import com.example.cowinnotifier.viewmodel.ActivityViewModel
import kotlinx.android.synthetic.main.fragment_pincode_search.*


class PincodeSearchFragment : Fragment() {

    private val viewModel: ActivityViewModel by activityViewModels()
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pincode_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        init()
    }

    private fun init() {

        spinner_age_filter.adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, AppConstants.AGE_LIMIT_FILTER_ARRAY)
        spinner_vaccine_filter.adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, AppConstants.VACCINE_FILTER_ARRAY)

        button_pincode_search.setOnClickListener {
            val pinEntered = edit_text_pincode.text.toString()
            val ageFilter = FilterUtil.getAgeFilterValueFromPosition(spinner_age_filter.selectedItemPosition)
            val vaccineFilter = FilterUtil.getVaccineFilterValueFromPosition(spinner_vaccine_filter.selectedItemPosition)

            if (pinEntered.length != 6) {
                Toast.makeText(mContext, "Please enter a valid pincode", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.clearSharedPreferenceData()
                viewModel.updateSharedPreferenceValue(AppConstants.PINCODE, pinEntered)
                viewModel.updateSharedPreferenceValue(AppConstants.AGE_LIMIT, ageFilter)
                viewModel.updateSharedPreferenceValue(AppConstants.VACCINE, vaccineFilter)

                viewModel.startActivityFromIntent(AppConstants.PINCODE, pinEntered, mContext, ageFilter, vaccineFilter)
            }
        }
    }
}