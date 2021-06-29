package com.example.cowinnotifier.utils

import androidx.room.TypeConverter

class ArrayListConverter {

    @TypeConverter
    fun stringToArrayList(string: String): ArrayList<String> {
        return string.split(",") as ArrayList<String>
    }

    @TypeConverter
    fun arrayListToString(arrayList: ArrayList<String>): String {
        return arrayList.joinToString(separator = ",")
    }
}