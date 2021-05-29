package ru.geekbrains.weatherapp.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
    fun getWeatherFromLocalStorageRus(): List<WeatherCategory>
    fun getWeatherFromLocalStorageWorld(): List<WeatherCategory>
}