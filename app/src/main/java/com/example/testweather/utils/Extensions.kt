package com.example.testweather.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


private var locale = Locale.ENGLISH

class SavingLocale(private val context: Context){
    fun setupLocale(lat: Double, lon: Double) {
        val maxResult = 1
        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses = geocoder.getFromLocation(lat, lon, maxResult)
        if (!addresses.isNullOrEmpty()) {
            locale = addresses.first()?.locale ?: Locale.ENGLISH
        }
    }
}



fun Int.toDateLongString(): String {
    val date = Date(this * 1000L)

    val dayFormat = SimpleDateFormat("EEEE", locale)
    val monthFormat = SimpleDateFormat("MMMM", locale)
    val dateFormat = SimpleDateFormat("dd", locale)

    val dayOfWeek = dayFormat.format(date)
    val month = monthFormat.format(date)
    val dayOfMonth = dateFormat.format(date)

    return "$dayOfWeek $month $dayOfMonth"
}

fun Int.toDateShortString(): String {
    val date = Date(this * 1000L)

    val dayFormat = SimpleDateFormat("EEEE", locale)
    val dateFormat = SimpleDateFormat("dd", locale)

    val dayOfWeek = dayFormat.format(date).slice(0..2)
    val dayOfMonth = dateFormat.format(date)

    return "$dayOfWeek $dayOfMonth"
}

fun Int.toTimeString(): String {
    val date = Date(this * 1000L)
    val formatter = SimpleDateFormat("HH:mm", locale)

    return formatter.format(date)
}

fun Int.toDayOfMonth(): Int {
    val date = Date(this * 1000L)

    val dateFormat = SimpleDateFormat("dd", locale)

    return dateFormat.format(date).toInt()
}

fun Double.toCelsius(): Float {
    val celsiusDouble = (this - 273.15)
    return celsiusDouble.roundToHalf()
}

fun Double.roundToHalf(): Float {
    return (this * 2).roundToInt() / 2f
}

fun Float.roundTo5(): Int {
    val numberRemainder = this % 5
    return (this - numberRemainder).roundToInt()
}