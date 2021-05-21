package ru.geekbrains.weatherapp.model

class RepositoryImpl : Repository {

    override fun getWeatherFromLocalStorage(): Weather {
        // имитируем сбой загрузки данных
        val rnds = (0..10).random()
        if (rnds >= 5) {
            throw LoadingException("Loading from local storage failed")
        }
        return Weather()
    }

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }
}

class LoadingException(s: String) : Throwable()