package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.ViewBindingDelegate
import ru.geekbrains.weatherapp.databinding.FragmentMainBinding
import ru.geekbrains.weatherapp.model.City
import ru.geekbrains.weatherapp.model.Weather
import ru.geekbrains.weatherapp.viewmodel.MainState
import ru.geekbrains.weatherapp.viewmodel.MainViewModel

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        fun newInstance() = MainFragment()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: City)
    }

    private val binding: FragmentMainBinding by ViewBindingDelegate(FragmentMainBinding::bind)

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) } // привязка viewModel к жизненному циклу фрагмента MainFragment

    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(city: City) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(Weather(city)))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })
    private var isDataSetRus: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // подписываемся на изменения LiveData<AppState>
        // связка с жизненным циклом вьюхи(!) фрагмента MainFragment
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        showListOfTowns()

        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
    }

    private fun showListOfTowns() {
        requireActivity().let {
            if (it.getPreferences(Context.MODE_PRIVATE).getBoolean("LIST_OF_TOWNS_KEY", true)) {
                viewModel.getCityCategoriesRus()
            } else {
                changeWeatherDataSet()
            }
        }
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getCityCategoriesWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getCityCategoriesRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        isDataSetRus = !isDataSetRus
        saveListOfTowns(isDataSetRus)
    }

    private fun saveListOfTowns(isDataSetRus: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean("LIST_OF_TOWNS_KEY", isDataSetRus)
                apply()
            }
        }
    }

    // renderData() вызывается Observer'ом при изменении данных LiveData
    private fun renderData(mainState: MainState) {
        when (mainState) {
            is MainState.Loading -> {
                binding.mainFragmentLoadingLayout.loadingLayout.visibility = View.VISIBLE // отображаем прогрессбар
                Toast.makeText(context, getString(R.string.loading_mess), Toast.LENGTH_SHORT).show()
            }
            is MainState.Success -> {
                binding.mainFragmentLoadingLayout.loadingLayout.visibility = View.GONE // скрываем прогрессбар
                adapter.setCityCategories(mainState.data)
                Toast.makeText(context, getString(R.string.loading_success_mess), Toast.LENGTH_LONG).show()
            }
            is MainState.Error -> {
                binding.mainFragmentLoadingLayout.loadingLayout.visibility = View.GONE // скрываем прогрессбар
                Toast.makeText(context, getString(R.string.loading_failed_mess), Toast.LENGTH_SHORT).show()
            }
        }
    }
}