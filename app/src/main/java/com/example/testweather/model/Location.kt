package com.example.testweather.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.testweather.utils.Constants

@Entity(tableName = "location")
data class Location(
    @PrimaryKey
    val id:Int = 0,
    val latitude: Double,
    val longitude: Double,
    val nameCity: String = Constants.MY_LOCATION
)

