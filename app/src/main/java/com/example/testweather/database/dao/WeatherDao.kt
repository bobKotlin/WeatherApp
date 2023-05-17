package com.example.testweather.database.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testweather.model.weather.WeatherListFor3Hours
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @WorkerThread
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherListFor3Hours(model: WeatherListFor3Hours)

    @WorkerThread
    @Query("SELECT * FROM weather_listFor_3_hours")
    fun getWeatherListFor3Hours(): Flow<WeatherListFor3Hours?>

}