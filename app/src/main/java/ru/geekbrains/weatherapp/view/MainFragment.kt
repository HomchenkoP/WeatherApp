package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.databinding.FragmentMainBinding
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

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    private var _binding: FragmentMainBinding? = null
    private val binding
        // геттер переменной binding
        get(): FragmentMainBinding = _binding!!

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) } // привязка viewModel к жизненному циклу фрагмента MainFragment
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener
    {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(weather))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })
    private var isDataSetRus: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // подписываемся на изменения LiveData<AppState>
        // связка с жизненным циклом вьюхи(!) фрагмента MainFragment
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()

        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        isDataSetRus = !isDataSetRus
    }

    // renderData() вызывается Observer'ом при изменении данных LiveData
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE // отображаем прогрессбар
                Toast.makeText(context, getString(R.string.loading_mess), Toast.LENGTH_SHORT).show()
            }
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE // скрываем прогрессбар
                adapter.setWeatherCategory(appState.weatherData)
                Toast.makeText(context, getString(R.string.loading_success_mess), Toast.LENGTH_LONG).show()
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE // скрываем прогрессбар
                Toast.makeText(context, getString(R.string.loading_failed_mess), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.removeListener()
    }
}