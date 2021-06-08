package ru.geekbrains.weatherapp.model

data class CityCategory(
    val title: String,
    val items: List<City>
)

fun getWorldCityCategories() = listOf(
        CityCategory("погода в мире", getWorldCities()),
        CityCategory("погода в России", getRussianCities())
    )

fun getRussianCityCategories() =  listOf(
        CityCategory("погода в России", getRussianCities()),
        CityCategory("погода в мире", getWorldCities())
    )