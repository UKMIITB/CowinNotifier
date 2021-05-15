package com.example.cowinnotifier.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cowinnotifier.repository.APIRepository
import com.example.cowinnotifier.repository.retrofit.APIService
import com.example.cowinnotifier.repository.retrofit.RetrofitClient

class MainActivityViewModel : ViewModel() {

    private var apiService: APIService = RetrofitClient.apiService
    private var apiRepository: APIRepository = APIRepository(apiService)

    suspend fun getStateList() = apiRepository.getStateList()

    suspend fun getDistrictList(stateId: Long) = apiRepository.getDistrictList(stateId)
}