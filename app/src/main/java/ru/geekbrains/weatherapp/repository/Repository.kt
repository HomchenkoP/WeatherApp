package ru.geekbrains.weatherapp.repository

import ru.geekbrains.weatherapp.model.City
import ru.geekbrains.weatherapp.model.CityCategory
import ru.geekbrains.weatherapp.model.Weather

interface Repository {
    fun getWeather(city: City): Weather
    fun getCityCategoriesRus(): List<CityCategory>
    fun getCityCategoriesWorld(): List<CityCategory>
}