package ru.geekbrains.weatherapp.model

interface Repository {
    fun getWeather(city: City): Weather
    fun getCityCategoriesRus(): List<CityCategory>
    fun getCityCategoriesWorld(): List<CityCategory>
}