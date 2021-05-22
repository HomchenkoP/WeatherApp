package ru.geekbrains.weatherapp.model

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0
)

fun getDefaultCity() = City("Екатеринбург", 56.8519, 60.6122)