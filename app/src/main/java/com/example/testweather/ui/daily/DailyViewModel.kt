package com.example.testweather.ui.daily

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweather.database.converters.WeatherForDayConverter
import com.example.testweather.model.weather.WeatherFor3Hours
import com.example.testweather.model.weather.WeatherForDay
import com.example.testweather.model.weather.WeatherListFor3Hours
import com.example.testweather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {


    private val weatherListDays = weatherRepository.weatherListFor3Hours

    private val _weatherOneDay: MutableStateFlow<WeatherForDay?> = MutableStateFlow(null)
    val weatherOneDay = _weatherOneDay.asStateFlow()

    private val weatherForDayConverter = WeatherForDayConverter()


    fun getWeatherByDay(weatherForDay: WeatherForDay) {
        _weatherOneDay.value = weatherForDay
    }

    fun getWeatherTodayDay() {
        viewModelScope.launch {
            weatherListDays.collect {
                if (!it?.list.isNullOrEmpty()){
                    Log.d("TagProj", "getWeatherTodayDay: ")
                    _weatherOneDay.value = weatherForDayConverter.listWeatherFor3HoursToListWeatherForDay(it?.list).first()
                }

            }
        }

    }


}