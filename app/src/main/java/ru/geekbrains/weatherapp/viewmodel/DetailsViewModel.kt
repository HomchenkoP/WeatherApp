package ru.geekbrains.weatherapp.viewmodel

import ru.geekbrains.weatherapp.model.WeatherDTO
import ru.geekbrains.weatherapp.model.convertDtoToModel
import ru.geekbrains.weatherapp.repository.DetailsRepository
import ru.geekbrains.weatherapp.repository.DetailsRepositoryImpl
import ru.geekbrains.weatherapp.repository.RemoteDataSource

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    val detailsLiveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getWeather(lat: Double, lon: Double) {
        detailsLiveData.value = DetailsState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    private val callBack = object :
        Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    DetailsState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(DetailsState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: WeatherDTO): DetailsState {
            val fact = serverResponse.fact
            return if (fact == null || fact.temp == null || fact.feelsLike == null || fact.condition.isNullOrEmpty()) {
                DetailsState.Error(Throwable(CORRUPTED_DATA))
            } else {
                DetailsState.Success(convertDtoToModel(serverResponse))
            }
        }
    }
}