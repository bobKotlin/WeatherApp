package com.example.testweather.database.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testweather.model.Location

@Dao
interface LocationDao {

    @WorkerThread
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    @WorkerThread
    @Query("SELECT * FROM location")
    suspend fun getLastLocation(): Location?

}