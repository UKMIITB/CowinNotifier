package com.example.cowinnotifier.repository

import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.repository.room.Dao
import javax.inject.Inject

class APIRepository @Inject constructor(private val apiService: APIService, private val dao: Dao) {

    suspend fun getStateListFromServer() = apiService.getStateList()

    suspend fun getDistrictListFromServer(stateId: Long) = apiService.getDistrictList(stateId)

    suspend fun getCalendarByDistrict(district_id: String, date: String) =
        apiService.getCalendarByDistrict(district_id, date).centerList

    suspend fun getCalendarByPincode(pincode: String, date: String) =
        apiService.getCalendarByPin(pincode, date).centerList

    fun getStateListFromDB() = dao.getAllStatesList()

    fun getDistrictListFromDB(stateId: Long) = dao.getAllDistrictList(stateId)

    fun insertState(state: State) = dao.insertState(state)

    fun insertDistrict(district: District) = dao.insertDistrict(district)
}