package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.ViewBindingDelegate
import ru.geekbrains.weatherapp.argumentNullable
import ru.geekbrains.weatherapp.databinding.FragmentDetailsBinding
import ru.geekbrains.weatherapp.model.Weather
import ru.geekbrains.weatherapp.model.WeatherDTO
import ru.geekbrains.weatherapp.repository.LocalBroadcastReceiver

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private var weatherData: Weather? by argumentNullable()

    companion object {

        fun newInstance(weather: Weather): DetailsFragment =
            DetailsFragment().apply {
                this.weatherData = weather
            }
    }

    private val binding: FragmentDetailsBinding by ViewBindingDelegate(FragmentDetailsBinding::bind)

    private val onLoadListener: LocalBroadcastReceiver.LocalBroadcastReceiverListener =
        object : LocalBroadcastReceiver.LocalBroadcastReceiverListener {

            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(msg: String) {
                //Обработка ошибки
            }
        }

    private val loadResultsReceiver: LocalBroadcastReceiver by lazy {
        LocalBroadcastReceiver(onLoadListener, getActivity()?.getApplicationContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(loadResultsReceiver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        weatherData?.let { weatherData ->
            loadResultsReceiver.getWeather(weatherData.city.lat, weatherData.city.lon)
        }
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherData?.city
            cityName.text = city?.name
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city?.lat.toString(),
                city?.lon.toString()
            )
            weatherCondition.text = weatherDTO.fact?.condition
            temperatureValue.text = weatherDTO.fact?.temp.toString()
            feelsLikeValue.text = weatherDTO.fact?.feelsLike.toString()
        }
    }
}