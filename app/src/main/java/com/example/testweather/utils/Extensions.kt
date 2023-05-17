package com.example.testweather.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun Int.toDateLongString(): String {
    val date = Date(this * 1000L)

    val dayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
    val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
    val dateFormat = SimpleDateFormat("dd", Locale.ENGLISH)

    val dayOfWeek = dayFormat.format(date)
    val month = monthFormat.format(date)
    val dayOfMonth = dateFormat.format(date)

    return "$dayOfWeek $month $dayOfMonth"
}

fun Int.toDateShortString(): String {
    val date = Date(this * 1000L)

    val dayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
    val dateFormat = SimpleDateFormat("dd", Locale.ENGLISH)

    val dayOfWeek = dayFormat.format(date).slice(0..2)
    val dayOfMonth = dateFormat.format(date)

    return "$dayOfWeek $dayOfMonth"
}

fun Int.toDayOfMonth(): Int {
    val date = Date(this * 1000L)

    val dateFormat = SimpleDateFormat("dd", Locale.ENGLISH)

    return dateFormat.format(date).toInt()
}

fun Double.toCelsius(): Float {
    val celsiusDouble = (this - 273.15)
    return celsiusDouble.roundToHalf()
}

fun Double.roundToHalf(): Float {
    return (this * 2).roundToInt() / 2f
}