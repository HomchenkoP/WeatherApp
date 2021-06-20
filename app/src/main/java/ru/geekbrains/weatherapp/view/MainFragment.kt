package ru.geekbrains.weatherapp.view

import ru.geekbrains.weatherapp.R
import ru.geekbrains.weatherapp.ViewBindingDelegate
import ru.geekbrains.weatherapp.databinding.FragmentMainBinding
import ru.geekbrains.weatherapp.model.City
import ru.geekbrains.weatherapp.model.Weather
import ru.geekbrains.weatherapp.viewmodel.MainState
import ru.geekbrains.weatherapp.viewmodel.MainViewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.IOException

class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        fun newInstance() = MainFragment()

        private const val REQUEST_CODE = 12345
        private const val REFRESH_PERIOD = 60000L
        private const val MINIMAL_DISTANCE = 100f
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(city: City)
    }

    private val binding: FragmentMainBinding by ViewBindingDelegate(FragmentMainBinding::bind)

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) } // привязка viewModel к жизненному циклу фрагмента MainFragment

    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(city: City) {
            openDetailsFragment(city)
        }
    })

    private var isDataSetRus: Boolean = true

    private val onLocationListener = object : LocationListener {

        // пришли новые данные о местоположении
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it, location)
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        // пользователь включил GPS
        override fun onProviderEnabled(provider: String) {}

        // пользователь выключил GPS
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // подписываемся на изменения LiveData<AppState>
        // связка с жизненным циклом вьюхи(!) фрагмента MainFragment
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        showListOfTowns()

        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        binding.mainFragmentFABLocation.setOnClickListener { checkPermission() }
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
                binding.mainFragmentLoadingLayout.loadingLayout.visibility =
                    View.VISIBLE // отображаем прогрессбар
                Toast.makeText(context, getString(R.string.loading_mess), Toast.LENGTH_SHORT).show()
            }
            is MainState.Success -> {
                binding.mainFragmentLoadingLayout.loadingLayout.visibility =
                    View.GONE // скрываем прогрессбар
                adapter.setCityCategories(mainState.data)
                Toast.makeText(context, getString(R.string.loading_success_mess), Toast.LENGTH_LONG)
                    .show()
            }
            is MainState.Error -> {
                binding.mainFragmentLoadingLayout.loadingLayout.visibility =
                    View.GONE // скрываем прогрессбар
                Toast.makeText(context, getString(R.string.loading_failed_mess), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // запрашиваем у пользователя разрешение на доступ к геолокации телефона
    private fun checkPermission() {
        activity?.let {
            when {
                // разрешение получено ранее
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation() // получаем геолокацию
                }
                // отображаем диалоговое окно с объяснением, зачем нужен доступ к геолокации
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission() // запрашиваем разрешение на доступ
                }
            }
        }
    }

    // отображаем диалоговое окно с объяснением, зачем нужен доступ к геолокации
    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_meaasge))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                    requestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    // запрашиваем разрешение на доступ
    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    // получаем ответ пользователя
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0
                if ((grantResults.isNotEmpty())) {
                    for (i in grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }
                    // разрешение получено
                    if (grantResults.size == grantedPermissions) {
                        getLocation() // получаем геолокацию
                    } else {
                        // нет разрешения
                        showDialog(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    // нет разрешения
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }
                return
            }
        }
    }

    // информируем пользователя, что, если он хочет получать погоду по координатам, то нужно дать разрешение на доступ к GPS
    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    // получаем геолокацию
    private fun getLocation() {
        activity?.let { context ->
            // разрешение получено ранее
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // менеджер геолокаций
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        // запрашиваем геолокацию каждые 60 секунд или каждые 100 метров
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    // если выключён GPS, запрашиваем у LocationManager последнюю известную локацию
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        // информируем пользователя, что GPS не включён
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(context, location)
                        // информируем пользователя, что GPS не включён
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                // отображаем диалоговое окно с объяснением, зачем нужен доступ к геолокации
                showRationaleDialog()
            }
        }
    }

    //  получаем адрес по известным координатам
    private fun getAddressAsync(
        context: Context,
        location: Location
    ) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                // запрашиваем данные у сервера Google
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                binding.mainFragmentFABLocation.post {
                    // может прийти больше одного адреса, показываем только первый
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    //  спрашиваем, нужно ли получить погоду по определенному адресу
    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    openDetailsFragment(
                        City(
                            address,
                            location.latitude,
                            location.longitude
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    // открываем фрагмент с данными о погоде
    private fun openDetailsFragment(city: City) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                .add(R.id.container, DetailsFragment.newInstance(Weather(city)))
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }
}