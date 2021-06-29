package com.example.cowinnotifier.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.SearchParameter
import com.example.cowinnotifier.model.State
import com.example.cowinnotifier.utils.ArrayListConverter

@Database(
    entities = [State::class, District::class, SearchParameter::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(ArrayListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}