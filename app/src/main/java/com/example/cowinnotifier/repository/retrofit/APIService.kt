package com.example.cowinnotifier.repository.retrofit

import com.example.cowinnotifier.helper.Endpoint
import com.example.cowinnotifier.model.GetDistrictsAPIResponse
import com.example.cowinnotifier.model.GetStateAPIResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface APIService {
    @Headers("User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36")
    @GET(Endpoint.GET_STATES_URL)
    suspend fun getStateList(): GetStateAPIResponse

    @GET(Endpoint.GET_DISTRICTS_URL + "{stateId}")
    suspend fun getDistrictList(@Path("stateId") stateId: Long): GetDistrictsAPIResponse
}