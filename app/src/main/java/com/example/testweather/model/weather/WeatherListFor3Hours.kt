package com.example.testweather.model.weather

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_listFor_3_hours")
data class WeatherListFor3Hours(
    @PrimaryKey
    val id: Int = 0,
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherFor3Hours>,
    val message: Int
)

data class WeatherForDay(
    var maxTempInDayCelsius: Float,
    var minTempInDayCelsius: Float,
    var averageTempInDayCelsius: Float,
    var averageWindInDay: Float,
    var averageHumidityInDay: Float,
    var maxProbabilityRainInDay: Float,
    var stringDateLong: String,
    var stringDateShort: String,
    var descriptionSun: String,
    var listAverageTempInDayCelsius: List<Float>,
    var listStringTime: List<String>,
)

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)


data class WeatherFor3Hours(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: WeatherValues,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val visibility: Int,
    val weather: List<WeatherX>,
    val wind: Wind
)



data class Clouds(
    val all: Int
)

data class Sys(
    val pod: String
)

data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)

data class Rain(
    val `3h`: Double
)

data class WeatherValues(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Coord(
    val lat: Double,
    val lon: Double
)





