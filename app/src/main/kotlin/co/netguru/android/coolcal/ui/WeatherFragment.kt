package co.netguru.android.coolcal.ui

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.formatting.ValueFormatter
import co.netguru.android.coolcal.formatting.WeatherDecoder
import co.netguru.android.coolcal.weather.OpenWeatherMap
import co.netguru.android.coolcal.weather.WeatherResponse
import kotlinx.android.synthetic.main.fragment_weather.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class WeatherFragment : BaseFragment() {

    @Inject lateinit var openWeatherMap: OpenWeatherMap
    @Inject lateinit var weatherDecoder: WeatherDecoder
    @Inject lateinit var valueFormatter: ValueFormatter

    init {
        App.component.inject(this)
    }

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
        subscription = openWeatherMap.getWeather(latitude, longitude)
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

        weatherIconImageView.setImageResource(weatherDecoder.getIconRes(weather.icon))
        weatherDescriptionTextView.text = weather.description
        weatherMessageTextView.text = "Weather description" // todo
        weatherTemperatureTextView.text = valueFormatter.formatTemperature(data.main?.temperature)
        weatherPressureTextView.text = valueFormatter.formatPressure(data.main?.pressure)
        weatherWindTextView.text = valueFormatter.formatWind(data.wind)
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location != null) {
            requestWeather(location)
        }
    }
}
