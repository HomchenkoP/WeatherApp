package ru.geekbrains.weatherapp.model

data class WeatherCategory(
    val title: String,
    val items: List<Weather>
)

fun getWorldWeatherCategory() = listOf(
        WeatherCategory("погода в мире", getWorldCities()),
        WeatherCategory("погода в России", getRussianCities())
    )

fun getRussianWeatherCategory() =  listOf(
        WeatherCategory("погода в России", getRussianCities()),
        WeatherCategory("погода в мире", getWorldCities())
    )