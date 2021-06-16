package ru.geekbrains.weatherapp.repository.room

import android.app.Application
import androidx.room.Room

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {

        private var appInstance: App? = null
        private const val DB_NAME = "History.db"
        private val db: HistoryDataBase by lazy {
            if (appInstance != null)
                Room.databaseBuilder(
                    appInstance!!.applicationContext,
                    HistoryDataBase::class.java,
                    DB_NAME
                ).build()
            else {
                throw IllegalStateException("Application is null while creating DataBase")
            }
        }

        fun getHistoryDao(): HistoryDao = db.historyDao()
    }
}