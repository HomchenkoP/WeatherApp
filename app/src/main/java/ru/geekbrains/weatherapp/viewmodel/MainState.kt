package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.model.CityCategory

sealed class MainState {
    data class Success(val data: List<CityCategory>) : MainState()
    data class Error(val error: Throwable) : MainState()
    object Loading : MainState()
}