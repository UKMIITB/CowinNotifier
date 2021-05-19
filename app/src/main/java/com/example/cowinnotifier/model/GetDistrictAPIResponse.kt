package com.example.cowinnotifier.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class GetDistrictAPIResponse(
    @SerializedName("districts")
    val districtList: List<District>
)

@Entity(tableName = "District")
data class District(
    @PrimaryKey
    val district_id: Long, val district_name: String, var state_id: Long
) {
    override fun toString(): String {
        return district_name
    }
}
