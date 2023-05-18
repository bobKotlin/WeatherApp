package com.example.testweather.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweather.other.InternetChecking
import com.example.testweather.repository.WeatherRepository
import com.example.testweather.utils.Constants
import com.example.testweather.other.LocationRecipient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationRecipient: LocationRecipient,
    private val internetChecking: InternetChecking
) :
    ViewModel() {

    private val _progress = MutableStateFlow(false)
    val process = _progress.asStateFlow()


    fun getLocation(cityName: String? = null, checkOnInternet: (connected: Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (cityName != null){
                getWeatherFromServiceByCityName(cityName) {
                    checkOnInternet.invoke(internetChecking.isInternetConnected())
                }
                return@launch
            }

            val lastLocation = weatherRepository.getLastLocation()

            if (lastLocation == null || lastLocation.nameCity == Constants.MY_LOCATION) {
                locationRecipient.getLocation(lastLocation) { location ->
                    getWeatherFromServiceByLatLon(location.latitude, location.longitude) {
                        checkOnInternet.invoke(internetChecking.isInternetConnected())
                    }
                }
            } else {
                getWeatherFromServiceByCityName(lastLocation.nameCity) {
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
                    _progress.value = true
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
                    _progress.value = true
                } else
                    checkInternet()
            }

        }
    }


}