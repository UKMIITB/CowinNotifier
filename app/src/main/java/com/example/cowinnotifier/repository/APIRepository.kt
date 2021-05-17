package com.example.cowinnotifier.repository

import com.example.cowinnotifier.repository.retrofit.APIService

class APIRepository(private val apiService: APIService) {

    suspend fun getStateList() = apiService.getStateList().stateList

    suspend fun getDistrictList(stateId: Long) = apiService.getDistrictList(stateId).districtList

    suspend fun getCalendarByDistrict(district_id: String, date: String) =
        apiService.getCalendarByPin(district_id, date).centerList
}