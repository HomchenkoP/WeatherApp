package ru.geekbrains.weatherapp.repository.room

import ru.geekbrains.weatherapp.model.Weather

interface LocalRepository {

    interface RoomResultListener {
        fun onSuccess(history: List<Weather>)
        fun onFailed(throwable: Throwable)
    }

    fun getAllHistory(listener: RoomResultListener)
    fun saveEntity(weather: Weather)
}