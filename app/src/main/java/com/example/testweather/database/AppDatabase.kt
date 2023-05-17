package com.example.testweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.testweather.database.converters.CityConverter
import com.example.testweather.database.converters.WeatherFor3HoursConverter
import com.example.testweather.database.dao.LocationDao
import com.example.testweather.database.dao.WeatherDao
import com.example.testweather.model.Location
import com.example.testweather.model.weather.WeatherListFor3Hours

@TypeConverters(
    CityConverter::class,
    WeatherFor3HoursConverter::class
)
@Database(entities = [WeatherListFor3Hours::class, Location::class], version = 10, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao
    abstract fun getLocationDao(): LocationDao

    companion object {

        fun getDB(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "saved_database"
            )
                .fallbackToDestructiveMigration()
                .addTypeConverter(CityConverter())
                .addTypeConverter(WeatherFor3HoursConverter())
                .build()


    }
}