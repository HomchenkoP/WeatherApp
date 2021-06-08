package ru.geekbrains.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val name: String,
    val lat: Double,
    val lon: Double
) : Parcelable

fun getWorldCities() = listOf(
    City("Лондон", 51.5085, -0.1257),
    City("Токио", 35.6895, 139.6917),
    City("Париж", 48.8534, 2.3488),
    City("Берлин", 52.5200, 13.4049),
    City("Рим", 41.9028, 12.4964),
    City("Минск", 53.9045, 27.5615),
    City("Стамбул", 41.0082, 28.9784),
    City("Вашингтон", 38.9072, -77.0369),
    City("Киев", 50.4501, 30.523400000000038),
    City("Пекин", 39.9042, 116.4074)
)

fun getRussianCities() = listOf(
    City("Москва", 55.7558, 37.6173),
    City("Санкт-Петербург", 59.9343, 30.3351),
    City("Новосибирск", 55.0084, 82.9357),
    City("Екатеринбург", 56.8389, 60.6057),
    City("Нижний Новгород", 56.2965, 43.9361),
    City("Казань", 55.8304, 49.0661),
    City("Челябинск", 55.1644, 61.4368),
    City("Омск", 54.9884, 73.3242),
    City("Ростов-на-Дону", 47.2357, 39.7015),
    City("Уфа", 54.7388, 55.9721)
)