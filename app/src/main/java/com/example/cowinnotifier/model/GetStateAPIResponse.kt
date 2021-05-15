package com.example.cowinnotifier.model

import com.google.gson.annotations.SerializedName

data class GetStateAPIResponse(
    @SerializedName("states")
    val stateList: List<State>
)

data class State(val state_id: Long, val state_name: String)
