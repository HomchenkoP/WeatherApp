package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.databinding.MainFragmentBinding
import ru.geekbrains.weatherapp.model.Weather
import ru.geekbrains.weatherapp.viewmodel.AppState
import ru.geekbrains.weatherapp.viewmodel.MainViewModel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding
        // геттер переменной binding
        get(): MainFragmentBinding = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // привязка viewModel к жизненному циклу фрагмента MainFragment
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val observer = Observer<AppState> { state: AppState -> renderData(state) }
        // подписываемся на изменения LiveData<AppState>
        // связка с жизненным циклом вьюхи(!) фрагмента MainFragment
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getWeather()
    }

    // renderData() вызывается Observer'ом при изменении данных LiveData
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE // отображаем прогрессбар
                Toast.makeText(context, getString(R.string.loading_mess), Toast.LENGTH_SHORT).show()
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE // скрываем прогрессбар
                val weatherData = appState.weatherData
                setData(weatherData)
                Toast.makeText(context, getString(R.string.loading_success_mess), Toast.LENGTH_LONG).show()
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE // скрываем прогрессбар
                Toast.makeText(context, getString(R.string.loading_failed_mess), Toast.LENGTH_SHORT).show()
                //viewModel.getWeather()
            }
        }
    }

    private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.name
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat,
            weatherData.city.lon
        )
        binding.temperatureValue.text = weatherData.temperature.toString()
        binding.feelsLikeValue.text = weatherData.feelsLike.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}