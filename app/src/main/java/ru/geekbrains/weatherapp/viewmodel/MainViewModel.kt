package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.repository.LoadingException
import ru.geekbrains.weatherapp.repository.MockRepositoryImpl
import ru.geekbrains.weatherapp.repository.Repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(
    private val mainLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = MockRepositoryImpl()
) : ViewModel() {

    fun getLiveData() = mainLiveData

    private fun getDataFromLocalSource(isRussian: Boolean) {
        mainLiveData.value = AppState.Loading
        // имитация запроса к БД
        Thread {
            // три попытки
            for (tryCnt in 1..3) {
                mainLiveData.postValue(AppState.Loading)
                sleep(3000)
                try {
                    mainLiveData.postValue(
                        AppState.Success(
                            if (isRussian) repositoryImpl.getCityCategoriesRus()
                            else repositoryImpl.getCityCategoriesWorld()
                        )
                    )
                    break
                } catch (e: LoadingException) {
                    mainLiveData.postValue(AppState.Error(e))
                    sleep(1000)
                }
            }
        }.start()
    }

    fun getCityCategoriesRus() = getDataFromLocalSource(isRussian = true)

    fun getCityCategoriesWorld() = getDataFromLocalSource(isRussian = false)
}