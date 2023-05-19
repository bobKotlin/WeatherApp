package com.example.testweather.di

import android.content.Context
import com.example.testweather.database.AppDatabase
import com.example.testweather.database.dao.LocationDao
import com.example.testweather.database.dao.WeatherDao
import com.example.testweather.other.InternetChecking
import com.example.testweather.other.LocationRecipient
import com.example.testweather.other.SelectLocationMenu
import com.example.testweather.repository.WeatherRepository
import com.example.testweather.server.api.WeatherApi
import com.example.testweather.utils.Constants
import com.example.testweather.utils.SavingLocale
import com.example.testweather.utils.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiModule {


    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) =
        AppDatabase.getDB(context)

    @Singleton
    @Provides
    fun provideWeatherDao(appDatabase: AppDatabase) =
        appDatabase.getWeatherDao()

    @Singleton
    @Provides
    fun provideLocationDao(appDatabase: AppDatabase) =
        appDatabase.getLocationDao()

    @Singleton
    @Provides
    fun provideSavingLocale(@ApplicationContext context: Context) =
        SavingLocale(context)


    @Provides
    @Singleton
    fun provideWeatherRepository(weatherDao : WeatherDao, locationDao: LocationDao, savingLocale: SavingLocale): WeatherRepository {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.urlWeather)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherApi::class.java)

        return WeatherRepository(weatherDao,locationDao, weatherApi, savingLocale)
    }

    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context) =
        SharedPrefs(context)

    @Singleton
    @Provides
    fun provideLocationRecipient(@ApplicationContext context: Context) =
        LocationRecipient(context)

    @Singleton
    @Provides
    fun provideSelectLocationMenu(@ApplicationContext context: Context) =
        SelectLocationMenu(context)

    @Singleton
    @Provides
    fun provideInternetChecking(@ApplicationContext context: Context) =
        InternetChecking(context)



}