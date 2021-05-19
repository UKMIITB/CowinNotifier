package com.example.cowinnotifier.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.repository.APIRepository
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.repository.retrofit.RetrofitClient
import com.example.cowinnotifier.repository.room.Dao
import com.example.cowinnotifier.utils.CoroutineUtil

class ActivityViewModel(private val dao: Dao) : ViewModel() {

    private val apiService: APIService = RetrofitClient.apiService
    private val apiRepository: APIRepository = APIRepository(apiService, dao)

    private val stateList = MutableLiveData<List<State>>()
    private val districtList = MutableLiveData<List<District>>()
    private val centerList = MutableLiveData<List<Center>>()

    fun addStateListInDB() = CoroutineUtil.io {
        kotlin.runCatching {
            apiRepository.getStateList()
        }.onSuccess { stateAPIResponse ->

            val listOfStates = stateAPIResponse.stateList

            for (eachState in listOfStates) {
                dao.insertState(eachState)
            }

            stateList.postValue(listOfStates)

        }.onFailure { error ->
            Log.e("ActivityViewModel", "loadStateList: " + error.message.toString())
        }
    }

    fun getStateListFromDB(): LiveData<List<State>> {
        CoroutineUtil.io {
            stateList.postValue(dao.getAllStatesList())
        }

        return stateList
    }

    fun getDistrictListFromDB(stateId: Long): LiveData<List<District>> {
        CoroutineUtil.io {
            districtList.postValue(dao.getAllDistrictList(stateId))
        }

        return districtList
    }

    fun addDistrictListInDB(stateId: Long) = CoroutineUtil.io {
        kotlin.runCatching {
            apiRepository.getDistrictList(stateId)
        }.onSuccess { districtAPIResponse ->
            val listOfDistrict = districtAPIResponse.districtList

            for (eachDistrict in listOfDistrict) {
                eachDistrict.state_id = stateId
                dao.insertDistrict(eachDistrict)
            }

            districtList.postValue(listOfDistrict)
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