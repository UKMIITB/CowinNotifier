package com.example.cowinnotifier.repository.retrofit

import com.example.cowinnotifier.helper.Endpoint
import com.example.cowinnotifier.model.GetDistrictsAPIResponse
import com.example.cowinnotifier.model.GetStateAPIResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET(Endpoint.GET_STATES_URL)
    suspend fun getStateList(): GetStateAPIResponse

    @GET(Endpoint.GET_DISTRICTS_URL + "/{stateId}")
    suspend fun getDistrictList(@Path("stateId") stateId: Long): GetDistrictsAPIResponse
}