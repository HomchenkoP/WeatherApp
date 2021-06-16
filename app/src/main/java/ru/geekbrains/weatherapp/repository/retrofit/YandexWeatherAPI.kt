package ru.geekbrains.weatherapp.repository.retrofit

import ru.geekbrains.weatherapp.model.WeatherDTO

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YandexWeatherAPI {
    @GET("v2/forecast")
    fun getWeather(
        @Header("X-Yandex-API-Key") token: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String
    ): Call<WeatherDTO>
}