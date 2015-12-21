package co.netguru.android.coolcal.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import co.netguru.android.coolcal.R
import co.netguru.android.owm.api.OpenWeatherMap
import co.netguru.android.owm.api.WeatherResponse
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class CurrentWeatherFragment : Fragment() {

    val weatherIcon: ImageView by bindView<ImageView>(R.id.weather_icon)
    val technicalDescrView: TextView by bindView<TextView>(R.id.weather_technical_description)
    val informalDescrView: TextView by bindView<TextView>(R.id.weather_informal_description)
    val dayTempView: TextView by bindView<TextView>(R.id.weather_day_temp)
    val nightTempView: TextView by bindView<TextView>(R.id.weather_night_temp)
    val windView: TextView by bindView<TextView>(R.id.weather_wind)
    val pressureView: TextView by bindView<TextView>(R.id.weather_pressure)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestWeather()
    }

    private fun requestWeather() {
        // todo: location
        OpenWeatherMap.api.getWeather("Kraków")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response: WeatherResponse -> fillInfoWithData(response)
                }, {
                    error -> Log.e("ERROR", error.message)
                })
    }

    private fun fillInfoWithData(data: WeatherResponse) {
        val weather = data.weather[0]

        // todo: data formatting
        technicalDescrView.text = weather.description
        dayTempView.text = "${data.main?.temperature}\u00b0"
        nightTempView.text = "??"
        windView.text = "Wind: ${data.wind?.speed}"
        pressureView.text = "Pressure: ${data.main?.pressure}"
    }
}
