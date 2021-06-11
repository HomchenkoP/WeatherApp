package ru.geekbrains.weatherapp.view

import android.net.Uri
import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.ViewBindingDelegate
import ru.geekbrains.weatherapp.argumentNullable
import ru.geekbrains.weatherapp.databinding.FragmentDetailsBinding
import ru.geekbrains.weatherapp.model.Weather
import ru.geekbrains.weatherapp.viewmodel.DetailsState
import ru.geekbrains.weatherapp.viewmodel.DetailsViewModel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private var weatherData: Weather? by argumentNullable()

    companion object {

        fun newInstance(weather: Weather): DetailsFragment =
            DetailsFragment().apply {
                this.weatherData = weather
            }
    }

    private val binding: FragmentDetailsBinding by ViewBindingDelegate(FragmentDetailsBinding::bind)

    private val viewModel: DetailsViewModel by lazy { ViewModelProvider(this).get(DetailsViewModel::class.java) } // привязка viewModel к жизненному циклу фрагмента DetailsFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // подписываемся на изменения LiveData<DetailsState>
        // связка с жизненным циклом вьюхи(!) фрагмента DetailsFragment
        viewModel.detailsLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        weatherData?.city?.let { city -> viewModel.getWeather(city.lat, city.lon) }
    }

    private fun renderData(detailsState: DetailsState) {
        when (detailsState) {
            is DetailsState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE // отображаем прогрессбар
            }
            is DetailsState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE // скрываем прогрессбар
                displayWeather(detailsState.weatherData)
            }
            is DetailsState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE // скрываем прогрессбар
                Toast.makeText(context, getString(R.string.loading_failed_mess), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun displayWeather(weather: Weather) {
        // описание города из Bundle
        weatherData?.city?.let { city ->
            binding.cityName.text = city.name
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
        }
        // описание погоды от веб-сервиса
        binding.weatherCondition.text = weather.condition
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        weather.icon?.let {
            GlideToVectorYou.justLoadImage(
                activity,
                Uri.parse(String.format(getString(R.string.yandex_weather_icon_url),it)),
                binding.weatherIcon
            )
        }
    }
}