package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.ViewBindingDelegate
import ru.geekbrains.weatherapp.databinding.FragmentHistoryBinding
import ru.geekbrains.weatherapp.viewmodel.HistoryState
import ru.geekbrains.weatherapp.viewmodel.HistoryViewModel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class HistoryFragment : Fragment(R.layout.fragment_history) {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private val binding: FragmentHistoryBinding by ViewBindingDelegate(FragmentHistoryBinding::bind)

    private val viewModel: HistoryViewModel by lazy { ViewModelProvider(this).get(HistoryViewModel::class.java) } // привязка viewModel к жизненному циклу фрагмента HistoryFragment

    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyFragmentRecyclerview.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getAllHistory()
    }

    private fun renderData(historyState: HistoryState) {
        when (historyState) {
            is HistoryState.Loading -> {
                binding.historyFragmentRecyclerview.visibility = View.GONE
                binding.historyFragmentLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is HistoryState.Success -> {
                binding.historyFragmentRecyclerview.visibility = View.VISIBLE
                binding.historyFragmentLoadingLayout.loadingLayout.visibility = View.GONE
                adapter.setData(historyState.weatherData)
            }
            is HistoryState.Error -> {
                binding.historyFragmentRecyclerview.visibility = View.VISIBLE
                binding.historyFragmentLoadingLayout.loadingLayout.visibility = View.GONE
                Toast.makeText(context, getString(R.string.loading_failed_mess), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}