package com.example.cowinnotifier.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cowinnotifier.model.Center
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.repository.APIRepository
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.repository.retrofit.RetrofitClient

class ActivityViewModel : ViewModel() {

    private val apiService: APIService = RetrofitClient.apiService
    private val apiRepository: APIRepository = APIRepository(apiService)

    suspend fun loadStateList(): List<State> {
        return apiRepository.getStateList()
    }

    suspend fun loadDistrictList(stateId: Long): List<District> {
        return apiRepository.getDistrictList(stateId)
    }

    suspend fun getCalendarByDistrictList(district_id: String, date: String): List<Center> {
        return apiRepository.getCalendarByDistrict(district_id, date)
    }

    suspend fun getCalendarByPincodeList(pincode: String, date: String): List<Center> {
        return apiRepository.getCalendarByPincode(pincode, date)
    }
}