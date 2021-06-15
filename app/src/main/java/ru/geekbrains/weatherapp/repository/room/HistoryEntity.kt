package ru.geekbrains.weatherapp.repository.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.geekbrains.weatherapp.model.City
import ru.geekbrains.weatherapp.model.Weather

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temperature: Int,
    val condition: String
)

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, 0, it.condition)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.temperature, weather.condition)
}
