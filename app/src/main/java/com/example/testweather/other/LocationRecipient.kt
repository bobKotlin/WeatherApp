package com.example.testweather.other

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Looper
import com.example.testweather.database.dao.LocationDao
import com.example.testweather.model.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationRecipient(private val context: Context, private val locationDao: LocationDao) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @SuppressLint("MissingPermission")
    fun getLocation(lastLocationDao: Location?, callbackLocation: (location: Location) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lastLocation = locationResult.lastLocation

                val location: Location

                if (lastLocation == null) {
                    location = Location(
                        latitude = lastLocationDao?.latitude ?: 0.00,
                        longitude = lastLocationDao?.longitude ?: 0.00
                    )
                } else {
                    location = Location(
                        latitude = lastLocation.latitude,
                        longitude = lastLocation.longitude
                    )
                    fusedLocationClient.removeLocationUpdates(this)
                }

                callbackLocation.invoke(location)

                coroutineScope.launch {
                    locationDao.insertLocation(location)
                }


            }
        }


        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(true)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}