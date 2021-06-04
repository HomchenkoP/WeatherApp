package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.argumentNullable
import ru.geekbrains.weatherapp.databinding.FragmentDetailsBinding
import ru.geekbrains.weatherapp.model.Weather
import ru.geekbrains.weatherapp.model.WeatherDTO
import ru.geekbrains.weatherapp.model.WeatherLoader

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

class DetailsFragment : Fragment() {
    private var weatherData: Weather? by argumentNullable()

    companion object {

        fun newInstance(weather: Weather): DetailsFragment =
            DetailsFragment().apply {
                this.weatherData = weather
            }
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        // геттер переменной binding
        get(): FragmentDetailsBinding = _binding!!

    private val onLoadListener: WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {

            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) {
                //Обработка ошибки
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        weatherData?.let { weatherData ->
            WeatherLoader(onLoadListener, weatherData.city.lat, weatherData.city.lon).loadWeather()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}