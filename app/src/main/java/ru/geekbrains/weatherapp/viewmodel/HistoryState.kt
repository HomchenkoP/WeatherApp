package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.model.Weather

sealed class HistoryState {
    data class Success(val weatherData: List<Weather>) : HistoryState()
    data class Error(val error: Throwable) : HistoryState()
    object Loading : HistoryState()
}