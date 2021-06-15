package ru.geekbrains.weatherapp.repository.room

import ru.geekbrains.weatherapp.model.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}