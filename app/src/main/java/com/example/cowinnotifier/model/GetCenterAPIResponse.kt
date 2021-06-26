package com.example.cowinnotifier.model

import com.google.gson.annotations.SerializedName

data class GetCenterAPIResponse(
    @SerializedName("centers")
    val centerList: List<Center>
)

data class Center(
    val name: String,
    val address: String,
    val sessions: List<Session>
)

data class Session(
    val date: String,
    val available_capacity: Long,
    val min_age_limit: Long,
    val vaccine: String,
    val available_capacity_dose1: Long,
    val available_capacity_dose2: Long
)
