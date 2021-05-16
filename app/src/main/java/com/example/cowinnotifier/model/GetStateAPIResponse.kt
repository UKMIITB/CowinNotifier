package com.example.cowinnotifier.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GetStateAPIResponse(
    @SerializedName("states")
    val stateList: List<State>
)

@Entity(tableName = "State")
data class State(
    @PrimaryKey
    val state_id: Long, val state_name: String
)
