package com.example.cowinnotifier.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.repository.APIRepository
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.repository.retrofit.RetrofitClient
import com.example.cowinnotifier.utils.CoroutineUtil

class MainActivityViewModel : ViewModel() {

    private val apiService: APIService = RetrofitClient.apiService
    private val apiRepository: APIRepository = APIRepository(apiService)

    val stateListLiveData: MutableLiveData<List<State>> = MutableLiveData()
    val districtListLiveData: MutableLiveData<List<District>> = MutableLiveData()

    fun loadStateList() {
        CoroutineUtil.io {
            val stateList = apiRepository.getStateList()
            stateListLiveData.postValue(stateList)
        }
    }

    fun loadDistrictList(stateId: Long) {
        CoroutineUtil.io {
            val districtList = apiRepository.getDistrictList(stateId)
            districtListLiveData.postValue(districtList)
        }
    }
}