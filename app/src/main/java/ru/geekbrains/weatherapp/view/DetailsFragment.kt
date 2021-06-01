package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.databinding.FragmentDetailsBinding
import ru.geekbrains.weatherapp.model.Weather
import ru.geekbrains.weatherapp.put

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class DetailsFragment : Fragment() {

    companion object {

        private const val BUNDLE_EXTRA = "weatherData"

        fun newInstance(weather: Weather): DetailsFragment =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    put(BUNDLE_EXTRA, weather)
                }
            }
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        // геттер переменной binding
        get(): FragmentDetailsBinding = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let { weatherData ->
            weatherData.city.also { city ->
                binding.cityName.text = city.name
                binding.cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    city.lat,
                    city.lon
                )
            }
            binding.temperatureValue.text = weatherData.temperature.toString()
            binding.feelsLikeValue.text = weatherData.feelsLike.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}