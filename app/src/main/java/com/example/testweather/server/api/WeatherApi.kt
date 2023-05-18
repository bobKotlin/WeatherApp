package com.example.testweather.server.api

import com.example.testweather.model.weather.WeatherListFor3Hours
import com.example.testweather.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET(Constants.urlWeatherListDays)
    suspend fun getWeatherByLatLon(
        @Query ("lat") lat: Double,
        @Query ("lon") lon: Double,
        @Query ("appid") apiKey: String,
    ): Response<WeatherListFor3Hours>

    @GET(Constants.urlWeatherListDays)
    suspend fun getWeatherByCityName(
        @Query ("q") cityName: String,
        @Query ("appid") apiKey: String,
    ): Response<WeatherListFor3Hours>
}

