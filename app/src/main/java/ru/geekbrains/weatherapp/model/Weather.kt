package ru.geekbrains.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0
) : Parcelable

fun getDefaultCity() = City("Екатеринбург", 56.8519, 60.6122)

fun getWorldCities() = listOf(
        Weather(City("Лондон", 51.5085, -0.1257), 1, 2),
        Weather(City("Токио", 35.6895, 139.6917), 3, 4),
        Weather(City("Париж", 48.8534, 2.3488), 5, 6),
        Weather(City("Берлин", 52.5200, 13.4049), 7, 8),
        Weather(City("Рим", 41.9028, 12.4964), 9, 10),
        Weather(City("Минск", 53.9045, 27.5615), 11, 12),
        Weather(City("Стамбул", 41.0082, 28.9784), 13, 14),
        Weather(City("Вашингтон", 38.9072, -77.0369), 15, 16),
        Weather(City("Киев", 50.4501, 30.523400000000038), 17, 18),
        Weather(City("Пекин", 39.9042, 116.4074), 19, 20)
    )

fun getRussianCities() =  listOf(
        Weather(City("Москва", 55.7558, 37.6173), 1, 2),
        Weather(City("Санкт-Петербург", 59.9343, 30.3351), 3, 3),
        Weather(City("Новосибирск", 55.0084, 82.9357), 5, 6),
        Weather(City("Екатеринбург", 56.8389, 60.6057), 7, 8),
        Weather(City("Нижний Новгород", 56.2965, 43.9361), 9, 10),
        Weather(City("Казань", 55.8304, 49.0661), 11, 12),
        Weather(City("Челябинск", 55.1644, 61.4368), 13, 14),
        Weather(City("Омск", 54.9884, 73.3242), 15, 16),
        Weather(City("Ростов-на-Дону", 47.2357, 39.7015), 17, 18),
        Weather(City("Уфа", 54.7388, 55.9721), 19, 20)
    )