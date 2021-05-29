package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.model.WeatherCategory

sealed class AppState {
    data class Success(val weatherData: List<WeatherCategory>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}