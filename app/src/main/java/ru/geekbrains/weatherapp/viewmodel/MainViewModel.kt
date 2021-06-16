package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.repository.LoadingException
import ru.geekbrains.weatherapp.repository.RepositoryImpl
import ru.geekbrains.weatherapp.repository.Repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(
    private val mainLiveData: MutableLiveData<MainState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData() = mainLiveData

    private fun getDataFromLocalSource(isRussian: Boolean) {
        mainLiveData.value = MainState.Loading
        // имитация запроса к БД
        Thread {
            // три попытки
            for (tryCnt in 1..3) {
                mainLiveData.postValue(MainState.Loading)
                sleep(3000)
                try {
                    mainLiveData.postValue(
                        MainState.Success(
                            if (isRussian) repositoryImpl.getCityCategoriesRus()
                            else repositoryImpl.getCityCategoriesWorld()
                        )
                    )
                    break
                } catch (e: LoadingException) {
                    mainLiveData.postValue(MainState.Error(e))
                    sleep(1000)
                }
            }
        }.start()
    }

    fun getCityCategoriesRus() = getDataFromLocalSource(isRussian = true)

    fun getCityCategoriesWorld() = getDataFromLocalSource(isRussian = false)
}