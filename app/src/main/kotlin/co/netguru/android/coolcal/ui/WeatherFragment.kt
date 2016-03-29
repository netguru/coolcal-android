package co.netguru.android.coolcal.ui

import android.location.Location
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.preferences.AppPreferences
import co.netguru.android.coolcal.rendering.WeatherDataFormatter
import co.netguru.android.coolcal.rendering.WeatherDecoder
import co.netguru.android.coolcal.utils.logError
import co.netguru.android.coolcal.weather.OpenWeatherMap
import co.netguru.android.coolcal.weather.WeatherResponse
import co.netguru.android.coolcal.utils.updateNeeded
import co.netguru.android.coolcal.rendering.WeatherDescriptionHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_weather.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class WeatherFragment : BaseFragment() {

    init {
        App.component.inject(this)
    }

    @Inject lateinit var openWeatherMap: OpenWeatherMap
    @Inject lateinit var weatherDecoder: WeatherDecoder
    @Inject lateinit var appPreferences: AppPreferences
    @Inject lateinit var weatherDataFormatter: WeatherDataFormatter
    @Inject lateinit var weatherDescription: WeatherDescriptionHelper
    @Inject lateinit var picasso: Picasso

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderWeatherData(appPreferences.lastWeather)

    }

    private fun requestWeather(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        subscription =
                openWeatherMap.getWeather(latitude, longitude)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: WeatherResponse ->
                    appPreferences.lastWeather = response
                    renderWeatherData(response)
                }, { error ->
                    logError(error.message)
                })
    }

    private fun renderWeatherData(data: WeatherResponse?) {
        if (data != null) {

            val weather = data.weather[0]

            picasso.load(weatherDecoder.getBackgroundsRes(weather.icon))
                    .fit()
                    .centerCrop()
                    .into(weatherBackground)
            content.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            weatherIconImageView.setImageResource(weatherDecoder.getIconRes(weather.icon))
            weatherDescriptionTextView.text = weather.description
            weatherMessageTextView.text = weatherDescription.getDescription(weather.icon, data.main?.temperature, data.main?.pressure)
            weatherTemperatureTextView.text = weatherDataFormatter.formatTemperature(data.main?.temperature)
            weatherPressureTextView.text = weatherDataFormatter.formatPressure(data.main?.pressure)
            weatherWindTextView.text = weatherDataFormatter.formatWind(data.wind?.speed, data.wind?.deg)
        }
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location != null && updateNeeded(appPreferences.lastWeatherSync)) {
            requestWeather(location)
        }
    }
}
