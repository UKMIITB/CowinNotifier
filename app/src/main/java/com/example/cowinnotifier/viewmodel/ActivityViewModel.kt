package com.example.cowinnotifier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.repository.APIRepository
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

    fun getCalendarByDistrictList(district_id: String, date: String): LiveData<List<Center>> {
        CoroutineUtil.io {
            kotlin.runCatching {
                apiRepository.getCalendarByDistrict(district_id, date)
            }.onSuccess { centerAPIResponse ->
                centerList.postValue(centerAPIResponse)
            }
        }
        return centerList
    }


    fun getCalendarByPincodeList(pincode: String, date: String): LiveData<List<Center>> {
        CoroutineUtil.io {
            kotlin.runCatching {
                apiRepository.getCalendarByPincode(pincode, date)
            }.onSuccess { centerAPIResponse ->
                centerList.postValue(centerAPIResponse)
            }
        }
        return centerList
    }
}