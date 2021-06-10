package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.MainBroadcastReceiver
import ru.geekbrains.weatherapp.R

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        MainBroadcastReceiver(this).let {
            lifecycle.addObserver(it)
        }
    }
}