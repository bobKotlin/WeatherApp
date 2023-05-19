package com.example.testweather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweather.other.InternetChecking
import com.example.testweather.repository.WeatherRepository
import com.example.testweather.utils.Constants
import com.example.testweather.other.LocationRecipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRecipient: LocationRecipient,
    private val internetChecking: InternetChecking
) :
    ViewModel() {

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()


    fun updateWeather(cityName: String? = null, checkOnInternet: (connected: Boolean) -> Unit) {
        _isLoaded.value = false
        viewModelScope.launch(Dispatchers.IO) {
            if (cityName != null) {
                getWeatherFromServiceByCityName(cityName) {
                    checkOnInternet.invoke(internetChecking.isInternetConnected())
                }
                return@launch
            }

            val lastLocation = weatherRepository.getLastLocation()


            locationRecipient.getLocation(lastLocation) { location ->
                getWeatherFromServiceByLatLon(location.latitude, location.longitude) {
                    checkOnInternet.invoke(internetChecking.isInternetConnected())
                }
            }


        }

    }

    private fun getWeatherFromServiceByLatLon(
        lat: Double,
        lon: Double,
        checkInternet: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherFromServiceByLatLon(lat, lon) { weatherListDays ->
                if (weatherListDays != null) {
                    weatherRepository.saveWeather(weatherListDays)

                    _isLoaded.value = true
                } else
                    checkInternet()
            }

        }
    }

    private fun getWeatherFromServiceByCityName(cityName: String, checkInternet: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherFromServiceByCityName(cityName) { weatherListDays ->
                if (weatherListDays != null) {
                    weatherRepository.saveWeather(weatherListDays)
                    _isLoaded.value = true
                } else
                    checkInternet()
            }

        }
    }


}