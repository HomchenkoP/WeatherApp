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
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME
                        )
                            .allowMainThreadQueries() // Внимание! К БД запрещено обращаться в основном потоке. Этот метод используется исключительно в качестве примера или тестирования.
                            .build()
                    }
                }
            }

            return db!!.historyDao()
        }
    }
}