package com.example.cowinnotifier.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cowinnotifier.utils.ArrayListConverter

@Entity(tableName = "Search")
data class SearchParameter(
    @PrimaryKey
    val id: Int,
    @TypeConverters(ArrayListConverter::class)
    val vaccineList: ArrayList<String>,
    val dose: String,
    val minAge: Long,
    val locationKey: String,
    val locationValue: String,
)
