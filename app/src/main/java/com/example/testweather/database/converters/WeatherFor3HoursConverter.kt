package com.example.testweather.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.testweather.model.weather.WeatherFor3Hours
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class WeatherFor3HoursConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToListWeatherFor3Hours(value: String): List<WeatherFor3Hours?> {
        val listType = object : TypeToken<List<WeatherFor3Hours?>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun listWeatherFor3HoursToString(weatherFor3HoursList: List<WeatherFor3Hours?>): String {
        return gson.toJson(weatherFor3HoursList)
    }

    fun stringToWeatherFor3Hours(value: String): WeatherFor3Hours {
        return gson.fromJson(value, WeatherFor3Hours::class.java)
    }

    fun weatherFor3HoursToString(weatherFor3HoursList: WeatherFor3Hours?): String {
        return gson.toJson(weatherFor3HoursList)
    }

}