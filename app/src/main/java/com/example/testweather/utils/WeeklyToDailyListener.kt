package com.example.testweather.utils

import com.example.testweather.model.weather.WeatherFor3Hours
import com.example.testweather.model.weather.WeatherForDay

interface WeeklyToDailyListener {
    fun onClickItemWeather(weatherForDay: WeatherForDay)
}