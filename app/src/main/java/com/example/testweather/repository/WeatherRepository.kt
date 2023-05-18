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
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

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
            lastLocationDao.collect {
                lastLocation = it
            }
        }
    }

    suspend fun getWeatherFromServiceByLatLon(
        lat: Double,
        lon: Double,
        result: (weather5Days: WeatherListFor3Hours?) -> Unit
    ) {
        try {
            val callResult = weatherApi.getWeatherByLatLon(lat, lon, Constants.apiKeyWeather)

            handleWeatherRequest(callResult) { weather5Days ->
                result.invoke(weather5Days)
            }
        } catch (e: Exception) {
            result.invoke(null)
        }

    }

    suspend fun getWeatherFromServiceByCityName(
        cityName: String,
        result: (weather5Days: WeatherListFor3Hours?) -> Unit
    ) {
        try {
            val callResult = weatherApi.getWeatherByCityName(cityName, Constants.apiKeyWeather)

            handleWeatherRequest(callResult) { weather5Days ->
                result.invoke(weather5Days)
            }
        } catch (e: Exception) {
            result.invoke(null)
        }

    }

    fun saveWeather(weatherListFor3Hours: WeatherListFor3Hours) {
        coroutineScope.launch(Dispatchers.IO) {
            weatherDao.insertWeatherListFor3Hours(weatherListFor3Hours)
        }
    }

    fun getLastLocation(): Location? {
        return lastLocation
    }

    private fun handleWeatherRequest(
        callResult: Response<WeatherListFor3Hours>,
        result: (weather5Days: WeatherListFor3Hours?) -> Unit
    ) {
        if (callResult.isSuccessful) {
            coroutineScope.launch {
                val weather5Days = callResult.body()

                if (weather5Days == null) {
                    result.invoke(null)
                    return@launch
                }


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

                result.invoke(weather5Days)
            }
        } else
            result.invoke(null)
    }
}