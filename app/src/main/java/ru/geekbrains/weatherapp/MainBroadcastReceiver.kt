package ru.geekbrains.weatherapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MainBroadcastReceiver(private val context: Context?) : BroadcastReceiver(),
    LifecycleObserver {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            StringBuilder().apply {
                append("СООБЩЕНИЕ ОТ СИСТЕМЫ\n")
                append("Action: ${intent.action}")
                toString().also {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun registerReceiver() {
        context?.let {
            it.registerReceiver(this, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unregisterReceiver() {
        context?.let {
            it.unregisterReceiver(this)
        }
    }
}