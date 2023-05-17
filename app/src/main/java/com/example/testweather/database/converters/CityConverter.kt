package com.example.testweather.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.testweather.model.weather.City
import com.google.gson.Gson

@ProvidedTypeConverter
class CityConverter {

    private val gson = Gson()

    @TypeConverter
    fun cityToString(city: City): String {
        return gson.toJson(city)
    }

    @TypeConverter
    fun stringToCity(json: String): City {
        return gson.fromJson(json, City::class.java)
    }
}