package com.example.testweather.di

import android.content.Context
import com.example.testweather.database.dao.WeatherDao
import com.example.testweather.database.AppDatabase
import com.example.testweather.database.dao.LocationDao
import com.example.testweather.repository.WeatherRepository
import com.example.testweather.server.api.WeatherApi
import com.example.testweather.utils.Constants
import com.example.testweather.utils.LocationRecipient
import com.example.testweather.utils.SelectLocationMenu
import com.example.testweather.utils.SharedPrefs
import com.google.gson.GsonBuilder
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


    @Provides
    @Singleton
    fun provideWeatherRepository(weatherDao : WeatherDao, locationDao: LocationDao): WeatherRepository {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.urlWeather)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

        val weatherApi = retrofit.create(WeatherApi::class.java)

        return WeatherRepository(weatherDao,locationDao, weatherApi)
    }

    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context) =
        SharedPrefs(context)

    @Singleton
    @Provides
    fun provideLocationRecipient(@ApplicationContext context: Context, dao: LocationDao) =
        LocationRecipient(context, dao)

    @Singleton
    @Provides
    fun provideSelectLocationMenu(@ApplicationContext context: Context) =
        SelectLocationMenu(context)


}