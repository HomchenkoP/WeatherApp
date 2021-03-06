package ru.geekbrains.weatherapp.repository.retrofit

import ru.geekbrains.weatherapp.model.WeatherDTO

interface DetailsRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}