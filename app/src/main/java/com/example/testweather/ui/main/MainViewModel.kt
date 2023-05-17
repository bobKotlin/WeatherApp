package com.example.testweather.ui.main

import android.location.Location
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweather.repository.WeatherRepository
import com.example.testweather.utils.Constants
import com.example.testweather.utils.LocationRecipient
import com.example.testweather.utils.SelectLocationMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val selectLocationMenu: SelectLocationMenu,
    private val locationRecipient: LocationRecipient,
) :
    ViewModel() {

    private val _cityName: MutableStateFlow<String?> = MutableStateFlow(null)
    val cityName = _cityName.asStateFlow()


    init {
        viewModelScope.launch {
            val lastLocation = weatherRepository.getLastLocation()
            _cityName.value = lastLocation?.nameCity
        }

    }

    fun clickOnSelectLocation(textView: TextView) {
        selectLocationMenu.showMenu(textView) { cityName ->

            viewModelScope.launch(Dispatchers.IO) {
                val lastLocation = weatherRepository.getLastLocation()
                val newLocation = lastLocation?.copy(nameCity = cityName)

                if (lastLocation == null || lastLocation.nameCity != cityName)
                    getWeather(newLocation)

            }
        }
    }

    private fun getWeather(location: com.example.testweather.model.Location?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (location == null || location.nameCity == Constants.MY_LOCATION) {
                locationRecipient.getLocation(location) { location ->
                    getWeatherFromServiceByLatLon(location.latitude, location.longitude)
                }
            } else
                getWeatherFromServiceByCityName(location.nameCity)

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
            _cityName.value = weatherListDays.city.name

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

            _cityName.value = weatherListDays.city.name
        }
    }

}