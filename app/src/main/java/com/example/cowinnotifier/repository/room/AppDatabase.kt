package com.example.cowinnotifier.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State

@Database(entities = [State::class, District::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}