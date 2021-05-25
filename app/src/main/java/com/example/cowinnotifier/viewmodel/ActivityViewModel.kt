package com.example.cowinnotifier.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cowinnotifier.MyApplication
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.repository.APIRepository
import com.example.cowinnotifier.ui.ResultActivity
import com.example.cowinnotifier.utils.CoroutineUtil
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

    fun loadCalendarByDistrict(district_id: String, date: String) = CoroutineUtil.io {
        kotlin.runCatching {
            apiRepository.getCalendarByDistrict(district_id, date)
        }.onSuccess { centerAPIResponse ->
            centerList.postValue(centerAPIResponse)
        }
    }

    fun loadCalendarByPincode(pincode: String, date: String) = CoroutineUtil.io {
        kotlin.runCatching {
            apiRepository.getCalendarByPincode(pincode, date)
        }.onSuccess { centerAPIResponse ->
            centerList.postValue(centerAPIResponse)
        }
    }

    fun getSharedPreferenceValue(key: String): String? {
        return MyApplication.sharedPreferences.getString(key, "-1")
    }

    fun updateSharedPreferenceValue(key: String, value: String) {
        with(MyApplication.sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun clearSharedPreferenceData() {
        with(MyApplication.sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    fun startActivityFromIntent(key: String, value: String, context: Context) {
        val intent = Intent(context, ResultActivity::class.java).apply {
            putExtra(key, value)
        }
        context.startActivity(intent)
    }
}