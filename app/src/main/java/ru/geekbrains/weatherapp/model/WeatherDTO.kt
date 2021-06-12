package ru.geekbrains.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("fact")
    val fact: FactDTO?
)

data class FactDTO(
    @SerializedName("temp")
    val temp: Int?,
    @SerializedName("feels_like")
    val feelsLike: Int?,
    @SerializedName("condition")
    val condition: String?,
    @SerializedName("icon")
    val icon: String?
)

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.fact!!
    return Weather(
        getDefaultCity(),
        fact.temp!!,
        fact.feelsLike!!,
        fact.condition!!,
        fact.icon
    )
}