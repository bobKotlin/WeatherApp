package com.example.testweather.repository

import com.example.testweather.database.dao.LocationDao
import com.example.testweather.database.dao.WeatherDao
import com.example.testweather.model.Location
import com.example.testweather.model.weather.WeatherListFor3Hours
import com.example.testweather.server.api.WeatherApi
import com.example.testweather.utils.Constants
import com.example.testweather.utils.SavingLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val weatherDao: WeatherDao,
    private val locationDao: LocationDao,
    private val weatherApi: WeatherApi,
    private val savingLocale: SavingLocale,
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var lastLocation: Location? = null

    val weatherListFor3Hours = weatherDao.getWeatherListFor3Hours()
    val lastLocationDao = locationDao.getLastLocation()

    init {
        coroutineScope.launch {
            lastLocationDao.collect{
                lastLocation = it
            }
        }
    }
    suspend fun getWeatherFromServiceByLatLon(lat: Double, lon: Double): WeatherListFor3Hours {
        val weather5Days = weatherApi.getWeatherByLatLon(lat, lon, Constants.apiKeyWeather)
        val latitude = weather5Days.city.coord.lat
        val longitude = weather5Days.city.coord.lon
        locationDao.insertLocation(
            Location(
                latitude = latitude,
                longitude = longitude,
                nameCity = weather5Days.city.name
            )
        )
        savingLocale.setupLocale(latitude, longitude)
        return weather5Days
    }

    suspend fun getWeatherFromServiceByCityName(cityName: String): WeatherListFor3Hours {
        val weather5Days = weatherApi.getWeatherByCityName(cityName, Constants.apiKeyWeather)
        val latitude = weather5Days.city.coord.lat
        val longitude = weather5Days.city.coord.lon
        locationDao.insertLocation(
            Location(
                latitude = latitude,
                longitude = longitude,
                nameCity = weather5Days.city.name
            )
        )
        savingLocale.setupLocale(latitude, longitude)
        return weather5Days
    }

    suspend fun saveWeather(weatherListFor3Hours: WeatherListFor3Hours) {
        withContext(Dispatchers.IO) {
            weatherDao.insertWeatherListFor3Hours(weatherListFor3Hours)
        }
    }

    fun getLastLocation(): Location? {
        return lastLocation
    }
}