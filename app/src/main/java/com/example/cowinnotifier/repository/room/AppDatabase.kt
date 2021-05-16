package com.example.cowinnotifier.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cowinnotifier.model.District
import com.example.cowinnotifier.model.State

@Database(entities = [State::class, District::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        var instance: AppDatabase? = null

        fun getDatabaseInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "myDB").build()
            }

            return instance
        }
    }
}