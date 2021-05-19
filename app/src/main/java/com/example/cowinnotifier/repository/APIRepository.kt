package com.example.cowinnotifier.repository

import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.repository.room.Dao

class APIRepository(private val apiService: APIService, private val dao: Dao) {

    suspend fun getStateList() = apiService.getStateList()

    suspend fun getDistrictList(stateId: Long) = apiService.getDistrictList(stateId)

    suspend fun getCalendarByDistrict(district_id: String, date: String) =
        apiService.getCalendarByDistrict(district_id, date).centerList

    suspend fun getCalendarByPincode(pincode: String, date: String) =
        apiService.getCalendarByPin(pincode, date).centerList
}