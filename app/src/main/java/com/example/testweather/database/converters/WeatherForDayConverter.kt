package com.example.testweather.database.converters

import android.util.Log
import com.example.testweather.model.weather.WeatherFor3Hours
import com.example.testweather.model.weather.WeatherForDay
import com.example.testweather.utils.roundToHalf
import com.example.testweather.utils.toCelsius
import com.example.testweather.utils.toDayOfMonth
import com.example.testweather.utils.toDateLongString
import com.example.testweather.utils.toDateShortString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class WeatherForDayConverter {
    private val gson = Gson()


    fun stringToListWeatherForDay(value: String): List<WeatherForDay?> {
        val listType = object : TypeToken<List<WeatherFor3Hours?>>() {}.type
        return gson.fromJson(value, listType)
    }


    fun listWeatherForDayToString(weatherForDay: List<WeatherForDay?>): String {
        return gson.toJson(weatherForDay)
    }

    fun stringToWeatherForDay(value: String): WeatherForDay {
        return gson.fromJson(value, WeatherForDay::class.java)
    }

    fun weatherForDayToString(weatherForDay: WeatherForDay?): String {
        return gson.toJson(weatherForDay)
    }

    fun listWeatherFor3HoursToListWeatherForDay(weatherFor3HoursList: List<WeatherFor3Hours>?): List<WeatherForDay> {
        val listWeatherForDay = mutableListOf<WeatherForDay>()

        var generalDay = 0
        var generalMaxTemperature = 0f
        var generalMinTemperature = 0f
        var generalMinDateInteger = 0
        var generalMaxDateInteger = 0
        var generalMaxProbablyRain = 0f
        var generalAverageTemperature: Float? = null
        var generalAverageWind: Float? = null
        var generalAverageHumidity: Float? = null

        weatherFor3HoursList?.forEach { weatherFor3Hours ->

            val localDayDouble = weatherFor3Hours.dt
            val localDay = localDayDouble.toDayOfMonth()
            val localStringDateLong = weatherFor3Hours.dt.toDateLongString()
            val localStringDateShort = weatherFor3Hours.dt.toDateShortString()
            val localMaxTemperature = weatherFor3Hours.main.temp_max.toCelsius()
            val localMinTemperature = weatherFor3Hours.main.temp_min.toCelsius()
            val localAverageWind = weatherFor3Hours.wind.speed
            val localAverageHumidity = weatherFor3Hours.wind.speed
            val localMaxProbablyRain = weatherFor3Hours.pop
            val localAverageTemperature = (localMaxTemperature + localMinTemperature) / 2

            if (localMaxTemperature > generalMaxTemperature) {
                generalMaxTemperature = localMaxTemperature
            }

            if (localMinTemperature < generalMinTemperature) {
                generalMinTemperature = localMinTemperature
            }

            if (localDayDouble > generalMaxDateInteger) {
                generalMaxDateInteger = localDayDouble
            }

            if (localDayDouble < generalMinDateInteger) {
                generalMinDateInteger = localDayDouble
            }

            if (localMaxProbablyRain > generalMaxProbablyRain) {
                generalMaxProbablyRain = localMaxProbablyRain.roundToHalf()
            }

            if (generalAverageTemperature == null)
                generalAverageTemperature = localAverageTemperature.toDouble().roundToHalf()
            else
                generalAverageTemperature =
                    ((generalAverageTemperature!! + localAverageTemperature) / 2).toDouble().roundToHalf()

            if (generalAverageWind == null)
                generalAverageWind = localAverageWind.roundToHalf()
            else
                generalAverageWind =
                    ((generalAverageWind!! + localAverageWind) / 2).roundToHalf()

            if (generalAverageHumidity == null)
                generalAverageHumidity = localAverageHumidity.roundToHalf()
            else
                generalAverageHumidity =
                    ((generalAverageHumidity!! + localAverageHumidity) / 2).roundToHalf()



            if (localDay > generalDay) {
                generalDay = localDay

                val weatherForDay =
                    WeatherForDay(
                        maxTempInDayCelsius = generalMaxTemperature,
                        minTempInDayCelsius = generalMinTemperature,
                        averageTempInDayCelsius = generalAverageTemperature ?: 0f,
                        averageWindInDay = generalAverageWind ?: 0f,
                        averageHumidityInDay = generalAverageHumidity ?: 0f,
                        maxProbabilityRainInDay = generalMaxProbablyRain,
                        minDateInteger = generalMinDateInteger,
                        maxDateInteger = generalMaxDateInteger,
                        stringDateLong = localStringDateLong,
                        stringDateShort = localStringDateShort,
                        descriptionSun = weatherFor3Hours.weather.first().description,

                    )

                generalMaxTemperature = 0f
                generalMinTemperature = 0f
                generalMinDateInteger = 0
                generalMaxDateInteger = 0
                generalMaxProbablyRain = 0f
                generalAverageTemperature = null
                generalAverageWind = null
                generalAverageHumidity = null

                listWeatherForDay.add(weatherForDay)
            }

        }
        return listWeatherForDay
    }

}