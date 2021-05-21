package ru.geekbrains.weatherapp.model

class RepositoryImpl : Repository {

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }
}