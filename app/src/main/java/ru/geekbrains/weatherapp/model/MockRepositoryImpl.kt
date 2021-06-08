package ru.geekbrains.weatherapp.model

class MockRepositoryImpl : Repository {

    override fun getWeather(city: City): Weather =
        // имитируем сбой загрузки данных
        when ((0..10).random() >= 5) {
            true -> throw LoadingException("Loading from local storage failed")
            else -> getRandomWeather(city)
        }

    override fun getCityCategoriesRus() = getRussianCityCategories()

    override fun getCityCategoriesWorld() = getWorldCityCategories()

    fun getRandomWeather(city: City): Weather {
        val temperature = (-5..10).random()
        return Weather(city, temperature, temperature - 1)
    }
}

class LoadingException(s: String) : Throwable()