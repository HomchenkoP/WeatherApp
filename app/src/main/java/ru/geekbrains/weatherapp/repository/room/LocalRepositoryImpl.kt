package ru.geekbrains.weatherapp.repository.room

import ru.geekbrains.weatherapp.model.Weather

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

class LocalRepositoryImpl(private val localDataSource: HistoryDao) :
    LocalRepository {

    companion object {
        private val roomThreadExecutor = Executors.newSingleThreadExecutor();
        private val mainThreadHandler = Handler(Looper.getMainLooper());
    }

    override fun getAllHistory(listener: LocalRepository.RoomResultListener) {
        roomThreadExecutor.execute {
            val history: List<HistoryEntity> = localDataSource.all()
            mainThreadHandler.post { listener.onSuccess(convertHistoryEntityToWeather(history)) }
        }
    }

    override fun saveEntity(weather: Weather) {
        roomThreadExecutor.execute { localDataSource.insert(convertWeatherToEntity(weather)) }
    }
}