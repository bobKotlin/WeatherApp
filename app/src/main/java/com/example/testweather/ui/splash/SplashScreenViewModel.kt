package com.example.testweather.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweather.other.InternetChecking
import com.example.testweather.repository.WeatherRepository
import com.example.testweather.utils.Constants
import com.example.testweather.other.LocationRecipient
import com.example.testweather.ui.WeatherViewModel
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
) : WeatherViewModel(weatherRepository, locationRecipient, internetChecking) {


}