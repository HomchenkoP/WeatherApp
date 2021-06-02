package ru.geekbrains.weatherapp.model

class RepositoryImpl : Repository {

    override fun getWeatherFromLocalStorage(): Weather =
        // имитируем сбой загрузки данных
        when ((0..10).random() >= 5) {
            true -> throw LoadingException("Loading from local storage failed")
            else -> Weather()
        }

    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorageRus() = getRussianWeatherCategory()

    override fun getWeatherFromLocalStorageWorld() =  getWorldWeatherCategory()
}

class LoadingException(s: String) : Throwable()