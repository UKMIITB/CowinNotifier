package com.example.cowinnotifier.model

import com.google.gson.annotations.SerializedName

data class GetDistrictsAPIResponse(
    @SerializedName("districts")
    val districtList: List<District>
)

data class District(val district_id: Long, val district_name: String)
