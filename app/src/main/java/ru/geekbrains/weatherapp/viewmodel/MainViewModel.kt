package ru.geekbrains.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weatherapp.model.LoadingException
import ru.geekbrains.weatherapp.model.MockRepositoryImpl
import ru.geekbrains.weatherapp.model.Repository
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = MockRepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

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
                            if (isRussian) repositoryImpl.getCityCategoriesRus()
                            else repositoryImpl.getCityCategoriesWorld()
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

    fun getCityCategoriesRus() = getDataFromLocalSource(isRussian = true)

    fun getCityCategoriesWorld() = getDataFromLocalSource(isRussian = false)
}