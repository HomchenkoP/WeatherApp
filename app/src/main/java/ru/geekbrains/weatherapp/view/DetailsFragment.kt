package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.databinding.FragmentDetailsBinding
import ru.geekbrains.weatherapp.model.Weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class DetailsFragment : Fragment() {

    companion object {

        private const val BUNDLE_EXTRA = "weatherData"

        fun newInstance(weather: Weather): DetailsFragment {
            val fragment = DetailsFragment()
            // Передача параметра
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_EXTRA, weather)
            fragment.arguments = bundle
            return fragment
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

        val weatherData = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)
        if (weatherData != null) {
            binding.cityName.text = weatherData.city.name
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weatherData.city.lat,
                weatherData.city.lon
            )
            binding.temperatureValue.text = weatherData.temperature.toString()
            binding.feelsLikeValue.text = weatherData.feelsLike.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}