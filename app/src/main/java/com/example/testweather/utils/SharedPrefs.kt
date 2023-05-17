package com.example.testweather.utils

import android.content.Context

class SharedPrefs(private val context: Context) {

    private val sharedPrefs = context.getSharedPreferences(Constants.APP_SHARED_PREFS, Context.MODE_PRIVATE)

    var locationPermissionIsGranted : Boolean
        get() = sharedPrefs.getBoolean(Constants.LOCATION_PERMISSION_IS_GRANTED_PREFS, false)
        set(value) = sharedPrefs.edit().putBoolean(Constants.LOCATION_PERMISSION_IS_GRANTED_PREFS, value).apply()
}