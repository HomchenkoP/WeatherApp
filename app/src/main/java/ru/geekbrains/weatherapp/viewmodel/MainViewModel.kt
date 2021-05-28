package ru.geekbrains.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weatherapp.model.LoadingException
import ru.geekbrains.weatherapp.model.Repository
import ru.geekbrains.weatherapp.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        return liveDataToObserve
    }

    fun getWeather() {
        getDataFromLocalSource(isRussian = true)
    }

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        // имитация запроса к БД
        Thread {
            // три попытки
            for (tryCnt in 1..3) {
                liveDataToObserve.postValue(AppState.Loading)
                sleep(3000)
                try {
                    liveDataToObserve.postValue(
                        AppState.Success(
                            if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
                            else repositoryImpl.getWeatherFromLocalStorageWorld()
                        )
                    )
                    break
                } catch (e: LoadingException) {
                    liveDataToObserve.postValue(AppState.Error(e))
                    sleep(1000)
                }
            }
        }.start()
    }

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
}