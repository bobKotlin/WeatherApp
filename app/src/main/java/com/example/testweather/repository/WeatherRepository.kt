package com.example.testweather.repository

import android.util.Log
import com.example.testweather.database.dao.LocationDao
import com.example.testweather.database.dao.WeatherDao
import com.example.testweather.model.Location
import com.example.testweather.model.weather.WeatherListFor3Hours
import com.example.testweather.server.api.WeatherApi
import com.example.testweather.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val locationDao: LocationDao,
    private val weatherApi: WeatherApi
) {

    val weatherListFor3Hours = weatherDao.getWeatherListFor3Hours()

    suspend fun getWeatherFromServiceByLatLon(lat: Double, lon: Double): WeatherListFor3Hours {
        val weather5Days = weatherApi.getWeatherByLatLon(lat, lon, Constants.apiKeyWeather)
        val lastLocation = locationDao.getLastLocation()!!
        locationDao.insertLocation(lastLocation.copy(nameCity = weather5Days.city.name))

        return weather5Days
    }

    suspend fun getWeatherFromServiceByCityName(cityName: String): WeatherListFor3Hours {
        val weather5Days = weatherApi.getWeatherByCityName(cityName, Constants.apiKeyWeather)
        val lastLocation = locationDao.getLastLocation()!!

        locationDao.insertLocation(lastLocation.copy(nameCity = weather5Days.city.name))

        return weather5Days
    }

    suspend fun getLastLocation(): Location? {
        return locationDao.getLastLocation()
    }

    suspend fun saveWeather(weatherListFor3Hours: WeatherListFor3Hours) {
        withContext(Dispatchers.IO) {
            weatherDao.insertWeatherListFor3Hours(weatherListFor3Hours)
        }
    }
}