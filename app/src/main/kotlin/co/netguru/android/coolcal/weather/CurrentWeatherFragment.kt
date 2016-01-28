package co.netguru.android.coolcal.weather

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.BaseFragment
import co.netguru.android.coolcal.utils.AppPreferences
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class CurrentWeatherFragment : BaseFragment() {

    val weatherIcon: ImageView by bindView<ImageView>(R.id.weather_icon)
    val technicalDescrView: TextView by bindView<TextView>(R.id.weather_technical_description)
    val informalDescrView: TextView by bindView<TextView>(R.id.weather_informal_description)
    val dayTempView: TextView by bindView<TextView>(R.id.weather_temperature)
    val windView: TextView by bindView<TextView>(R.id.weather_wind)
    val pressureView: TextView by bindView<TextView>(R.id.weather_pressure)

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

        weatherIcon.setImageResource(WeatherDecoder.getIconRes(weather.icon))
        technicalDescrView.text = weather.description
        informalDescrView.text = "Weather description" // todo
        dayTempView.text = AppPreferences.formatTemperature(data.main?.temperature)
        pressureView.text = AppPreferences.formatPressure(data.main?.pressure)
        windView.text = AppPreferences.formatWind(data.wind)
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location != null) {
            requestWeather(location)
        }
    }
}
