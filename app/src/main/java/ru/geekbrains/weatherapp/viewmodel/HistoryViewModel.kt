package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.model.Weather
import ru.geekbrains.weatherapp.repository.room.App.Companion.getHistoryDao
import ru.geekbrains.weatherapp.repository.room.LocalRepository
import ru.geekbrains.weatherapp.repository.room.LocalRepositoryImpl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel(
    val historyLiveData: MutableLiveData<HistoryState> = MutableLiveData(),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {

    fun getAllHistory() {
        historyLiveData.value = HistoryState.Loading
        historyRepository.getAllHistory(roomResultListener)
    }

    private val roomResultListener =
        object : LocalRepository.RoomResultListener {

            override fun onSuccess(history: List<Weather>) {
                historyLiveData.value = HistoryState.Success(history)
            }

            override fun onFailed(throwable: Throwable) {
                //Обработка ошибки
            }
        }
}