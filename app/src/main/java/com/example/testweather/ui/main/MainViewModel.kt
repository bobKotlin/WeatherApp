package com.example.testweather.ui.main

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweather.repository.WeatherRepository
import com.example.testweather.utils.Constants
import com.example.testweather.other.LocationRecipient
import com.example.testweather.other.SelectLocationMenu
import com.example.testweather.model.Location
import com.example.testweather.other.InternetChecking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val selectLocationMenu: SelectLocationMenu
) :
    ViewModel() {

    fun getCityName():String{
        return weatherRepository.getLastLocation()?.nameCity ?: Constants.MY_LOCATION
    }
    fun clickOnSelectLocation(textView: TextView, cityNameListener:(cityName:String)->Unit) {
        selectLocationMenu.showMenu(textView, cityNameListener)
    }

}