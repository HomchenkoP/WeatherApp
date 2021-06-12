package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.model.Weather

sealed class DetailsState {
    data class Success(val weatherData: Weather) : DetailsState()
    data class Error(val error: Throwable) : DetailsState()
    object Loading : DetailsState()
}