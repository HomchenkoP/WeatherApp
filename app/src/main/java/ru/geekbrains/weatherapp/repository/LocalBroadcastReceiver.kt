package ru.geekbrains.weatherapp.repository

import ru.geekbrains.weatherapp.model.FactDTO
import ru.geekbrains.weatherapp.model.WeatherDTO

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class LocalBroadcastReceiver(
    private val listener: LocalBroadcastReceiverListener,
    private val context: Context?
) : BroadcastReceiver(), LifecycleObserver {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
            DETAILS_INTENT_EMPTY_EXTRA -> listener.onFailed("DETAILS_INTENT_EMPTY_EXTRA")
            DETAILS_DATA_EMPTY_EXTRA -> listener.onFailed("DETAILS_DATA_EMPTY_EXTRA")
            DETAILS_RESPONSE_EMPTY_EXTRA -> listener.onFailed("DETAILS_RESPONSE_EMPTY_EXTRA")
            DETAILS_REQUEST_ERROR_EXTRA -> listener.onFailed("DETAILS_REQUEST_ERROR_EXTRA")
            DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> listener.onFailed("DETAILS_REQUEST_ERROR_MESSAGE_EXTRA")
            DETAILS_URL_MALFORMED_EXTRA -> listener.onFailed("DETAILS_URL_MALFORMED_EXTRA")
            DETAILS_RESPONSE_SUCCESS_EXTRA -> listener.onLoaded(
                WeatherDTO(
                    FactDTO(
                        intent.getIntExtra(DETAILS_TEMP_EXTRA, 0),
                        intent.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, 0),
                        intent.getStringExtra(DETAILS_CONDITION_EXTRA),
                        intent.getStringExtra(DETAILS_ICON_EXTRA)
                    )
                )
            )
            else -> listener.onFailed("not DETAILS_LOAD_RESULT_EXTRA")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerReceiver() {
        context?.let {
            Toast.makeText(it, "DetailsFragment: ON_CREATE", Toast.LENGTH_SHORT).show()
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(this, IntentFilter(DETAILS_INTENT_FILTER))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterReceiver() {
        context?.let {
            Toast.makeText(it, "DetailsFragment: ON_DESTROY", Toast.LENGTH_SHORT).show()
            LocalBroadcastManager.getInstance(it)
                .unregisterReceiver(this)
        }
    }

    fun getWeather(lat: Double, lon: Double) {
        context?.let {
//            it.startService(Intent(it, DetailsService::class.java).apply {
            JobIntentService.enqueueWork(
                it,
                DetailsService::class.java,
                1,
                Intent(it, DetailsService::class.java).apply {
                    putExtra(LATITUDE_EXTRA, lat)
                    putExtra(LONGITUDE_EXTRA, lon)
                })
        }
    }

    interface LocalBroadcastReceiverListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(msg: String)
    }
}