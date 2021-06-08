package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.model.CityCategory

sealed class AppState {
    data class Success(val weatherData: List<CityCategory>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}