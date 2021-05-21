package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.model.Repository
import ru.geekbrains.weatherapp.model.RepositoryImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveDataToObserve
    }

    fun getWeather() {
        getDataFromLocalSource()
    }

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        // имитация запроса к БД
        Thread {
            sleep(3000) // временно! имитация задержки ответа БД
            liveDataToObserve.postValue(AppState.Success(repositoryImpl.getWeatherFromLocalStorage()))
        }.start()
    }
}