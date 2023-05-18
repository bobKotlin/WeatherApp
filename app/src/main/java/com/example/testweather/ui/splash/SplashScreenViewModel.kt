package com.example.testweather.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweather.repository.WeatherRepository
import com.example.testweather.utils.Constants
import com.example.testweather.utils.LocationRecipient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRecipient: LocationRecipient
) :
    ViewModel() {

    private val _progress = MutableStateFlow(false)
    val process = _progress.asStateFlow()



    fun getLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val lastLocation = weatherRepository.getLastLocation()


            if (lastLocation == null || lastLocation.nameCity == Constants.MY_LOCATION) {
                locationRecipient.getLocation(lastLocation) { location ->
                    getWeatherFromServiceByLatLon(location.latitude, location.longitude)
                }
            } else {
                getWeatherFromServiceByCityName(lastLocation.nameCity)
            }

        }

    }

    private fun getWeatherFromServiceByLatLon(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherListDays = weatherRepository.getWeatherFromServiceByLatLon(lat, lon)
            Log.d(
                "TagSplashViewModel",
                "getWeatherFromServiceByLatLon: ${weatherListDays.list.size}"
            )
            weatherRepository.saveWeather(weatherListDays)
            _progress.value = true
        }
    }

    private fun getWeatherFromServiceByCityName(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherListDays = weatherRepository.getWeatherFromServiceByCityName(cityName)
            Log.d(
                "TagSplashViewModel",
                "getWeatherFromServiceByCityName: ${weatherListDays.list.size}"
            )
            weatherRepository.saveWeather(weatherListDays)
            _progress.value = true
        }
    }


}