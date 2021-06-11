package ru.geekbrains.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0,
    val condition: String = "sunny",
    val icon: String? = ""
) : Parcelable

fun getDefaultCity() = City("Екатеринбург", 56.8519, 60.6122)