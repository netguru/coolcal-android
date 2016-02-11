package co.netguru.android.coolcal.weather

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.BaseFragment
import co.netguru.android.coolcal.utils.AppPreferences
import kotlinx.android.synthetic.main.fragment_weather.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class WeatherFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun requestWeather(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        subscription = OpenWeatherMap.api.getWeather(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: WeatherResponse ->
                    fillInfoWithData(response)
                }, { error ->
                    Log.e("ERROR", error.message)
                })
    }

    private fun fillInfoWithData(data: WeatherResponse) {
        val weather = data.weather[0]

        weatherIconImageView.setImageResource(WeatherDecoder.getIconRes(weather.icon))
        weatherDescriptionTextView.text = weather.description
        weatherMessageTextView.text = "Weather description" // todo
        weatherTemperatureTextView.text = AppPreferences.formatTemperature(data.main?.temperature)
        weatherPressureTextView.text = AppPreferences.formatPressure(data.main?.pressure)
        weatherWindTextView.text = AppPreferences.formatWind(data.wind)
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location != null) {
            requestWeather(location)
        }
    }
}
