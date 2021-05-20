package com.example.cowinnotifier.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State

@Dao
interface Dao {

    @Query("SELECT * FROM State ORDER BY state_name")
    fun getAllStatesList(): List<State>

    @Query("SELECT * FROM District WHERE state_id = :state_id ORDER BY district_name")
    fun getAllDistrictList(state_id: Long): List<District>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertState(vararg state: State)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDistrict(vararg district: District)
}