package com.example.testweather.permission

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class LocationPermissionAsker(private val appCompatActivity: AppCompatActivity) {

    fun showSystemDialog(onFinished: (isGranted : Boolean) -> Unit) {
        val notificationPermissionDialogLauncher = appCompatActivity.activityResultRegistry.register(
            "PermissionOpenDialog", ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            onFinished.invoke(isGranted)

        }
        notificationPermissionDialogLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}