package com.example.cowinnotifier.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cowinnotifier.MyApplication
import com.example.cowinnotifier.helper.AppConstants
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.repository.APIRepository
import com.example.cowinnotifier.ui.ResultActivity
import com.example.cowinnotifier.utils.CoroutineUtil
import com.example.cowinnotifier.utils.SessionUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val apiRepository: APIRepository) :
    ViewModel() {

    private val stateList = MutableLiveData<List<State>>()
    private val districtList = MutableLiveData<List<District>>()
    private val centerList = MutableLiveData<List<Center>>()

    fun observeStateList(): LiveData<List<State>> = stateList

    fun observeDistrictList(): LiveData<List<District>> = districtList

    fun observeCenterList(): LiveData<List<Center>> = centerList


    fun loadStateListData() = CoroutineUtil.io {

        val stateListFromDB = apiRepository.getStateListFromDB()

        if (stateListFromDB.isNullOrEmpty()) {
            val stateListFromServer = apiRepository.getStateListFromServer().stateList

            for (eachState in stateListFromServer) {
                apiRepository.insertState(eachState)
            }

            stateList.postValue(stateListFromServer)
        } else {
            stateList.postValue(stateListFromDB)
        }
    }

    fun loadDistrictListData(stateId: Long) = CoroutineUtil.io {

        val districtListFromDB = apiRepository.getDistrictListFromDB(stateId)

        if (districtListFromDB.isNullOrEmpty()) {
            val districtListFromServer =
                apiRepository.getDistrictListFromServer(stateId).districtList

            for (eachDistrict in districtListFromServer) {
                eachDistrict.state_id = stateId
                apiRepository.insertDistrict(eachDistrict)
            }

            districtList.postValue(districtListFromServer)
        } else {
            districtList.postValue(districtListFromDB)
        }
    }

    fun loadCalendarByDistrict(
        district_id: String,
        ageLimit: Long,
        vaccineList: ArrayList<String>,
        dose: String,
        date: String
    ) = CoroutineUtil.io {
        kotlin.runCatching {
            apiRepository.getCalendarByDistrict(district_id, date)
        }.onSuccess { centerAPIResponse ->
            val filteredCenterList =
                SessionUtil.filterCenterList(centerAPIResponse, ageLimit, vaccineList, dose)
            centerList.postValue(filteredCenterList)
        }
    }

    fun loadCalendarByPincode(
        pincode: String,
        ageLimit: Long,
        vaccineList: ArrayList<String>,
        dose: String,
        date: String
    ) = CoroutineUtil.io {
        kotlin.runCatching {
            apiRepository.getCalendarByPincode(pincode, date)
        }.onSuccess { centerAPIResponse ->
            val filteredCenterList =
                SessionUtil.filterCenterList(centerAPIResponse, ageLimit, vaccineList, dose)
            centerList.postValue(filteredCenterList)
        }
    }

    fun getStringSharedPreferenceValue(key: String): String? {
        return MyApplication.sharedPreferences.getString(key, "")
    }

    fun getLongSharedPreferenceValue(key: String): Long {
        return MyApplication.sharedPreferences.getLong(key, 0L)
    }

    fun updateSharedPreferenceValue(key: String, value: String) {
        with(MyApplication.sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun updateSharedPreferenceValue(key: String, value: Long) {
        with(MyApplication.sharedPreferences.edit()) {
            putLong(key, value)
            apply()
        }
    }

    fun clearSharedPreferenceData() {
        with(MyApplication.sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    fun startActivityFromIntent(
        searchTypeKey: String,
        searchTypeValue: String,
        context: Context,
        ageLimit: Long,
        doseSelected: String,
        vaccineList: ArrayList<String>
    ) {
        val intent = Intent(context, ResultActivity::class.java).apply {
            putExtra(searchTypeKey, searchTypeValue)
            putExtra(AppConstants.AGE_LIMIT, ageLimit)
            putExtra(AppConstants.DOSE, doseSelected)
            putExtra(AppConstants.VACCINE_LIST, vaccineList)
        }
        context.startActivity(intent)
    }
}