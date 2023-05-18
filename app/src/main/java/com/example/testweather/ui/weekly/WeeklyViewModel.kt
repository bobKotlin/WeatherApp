package com.example.testweather.ui.weekly

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweather.database.converters.WeatherForDayConverter
import com.example.testweather.model.weather.WeatherForDay
import com.example.testweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {


    private val weatherListFor3Hours = weatherRepository.weatherListFor3Hours

    private val _weatherForDay: MutableStateFlow<List<WeatherForDay>> = MutableStateFlow(listOf())
    val weatherForDay = _weatherForDay.asStateFlow()


    init {
        viewModelScope.launch {
            weatherListFor3Hours.collect {
                _weatherForDay.value =
                    WeatherForDayConverter().listWeatherFor3HoursToListWeatherForDay(it?.list)
            }
        }

    }

}