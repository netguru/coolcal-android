package co.netguru.android.coolcal.ui

import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.preferences.AppPreferences
import co.netguru.android.coolcal.rendering.WeatherDataFormatter
import co.netguru.android.coolcal.rendering.WeatherDecoder
import co.netguru.android.coolcal.rendering.WeatherDescriptionHelper
import co.netguru.android.coolcal.utils.logError
import co.netguru.android.coolcal.utils.updateNeeded
import co.netguru.android.coolcal.weather.OpenWeatherMap
import co.netguru.android.coolcal.weather.WeatherResponse
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringConfig
import com.facebook.rebound.SpringListener
import com.facebook.rebound.SpringSystem
import com.github.hujiaweibujidao.wava.Techniques
import com.github.hujiaweibujidao.wava.YoYo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_weather.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class WeatherFragment : BaseFragment(), SpringListener {

    init {
        App.component.inject(this)
    }

    private val SPRING_TENSION = 800.0
    private val SPRING_DAMPER = 20.0 //friction
    private val SHAKE_ANIMATION_DURATION = 500L
    private val SHAKE_ANIMATION_DELAY = 500L

    @Inject lateinit var openWeatherMap: OpenWeatherMap
    @Inject lateinit var weatherDecoder: WeatherDecoder
    @Inject lateinit var appPreferences: AppPreferences
    @Inject lateinit var weatherDataFormatter: WeatherDataFormatter
    @Inject lateinit var weatherDescription: WeatherDescriptionHelper
    @Inject lateinit var picasso: Picasso

    private lateinit var weatherIconSpringSystem: SpringSystem
    private lateinit var weatherIconSpring: Spring

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherMessageTextView.setOnClickListener { playShakeVideo() }
        initWeatherIconSpring()
        renderWeatherData(appPreferences.lastWeather)
    }

    override fun onPause() {
        super.onPause()
        weatherIconSpring.removeListener(this)
    }

    override fun onResume() {
        super.onResume()
        weatherIconSpring.addListener(this)
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location != null && updateNeeded(appPreferences.lastWeatherSync)) {
            requestWeather(location)
        }
    }

    override fun onSpringEndStateChange(spring: Spring?) {
        //Empty
    }

    override fun onSpringActivate(spring: Spring?) {
        //Empty
    }

    override fun onSpringUpdate(spring: Spring?) {
        val value = spring?.currentValue?.toFloat()
        val scale = 1f - (value!! * 0.5f)
        weatherIconImageView.scaleX = scale
        weatherIconImageView.scaleY = scale
    }

    override fun onSpringAtRest(spring: Spring?) {
        //Empty
    }

    /**
     * Initialize spring behaviour for weatherIconImageView.
     */
    private fun initWeatherIconSpring() {
        weatherIconSpringSystem = SpringSystem.create()
        weatherIconSpring = weatherIconSpringSystem.createSpring()
        val config = SpringConfig(SPRING_TENSION, SPRING_DAMPER)
        weatherIconSpring.springConfig = config

        weatherIconImageView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    weatherIconSpring.endValue = 1.0
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    weatherIconSpring.endValue = 0.0
                    return@setOnTouchListener true
                }
                else -> return@setOnTouchListener false
            }
        }
    }

    private fun requestWeather(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        subscription =
                openWeatherMap.getWeather(latitude, longitude)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response: WeatherResponse ->
                            if (response.weather.size != 0) {
                                appPreferences.lastWeather = response
                            }
                            renderWeatherData(response)
                        }, { error ->
                            logError(error.message)
                        })
    }

    private fun renderWeatherData(data: WeatherResponse?) {
        if (data != null) {

            val weather = data.weather[0]

            picasso.load(weatherDecoder.getBackgroundsRes(weather.icon))
                    .config(Bitmap.Config.ARGB_8888)
                    .fit()
                    .centerCrop()
                    .into(weatherBackground)
            content.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            weatherIconImageView.setImageResource(weatherDecoder.getIconRes(weather.icon))
            playShakeVideo()
            weatherDescriptionTextView.text = weather.description
            weatherMessageTextView.text = weatherDescription.getDescription(weather.icon, data.main?.temperature, data.main?.pressure)
            weatherTemperatureTextView.text = weatherDataFormatter.formatTemperature(data.main?.temperature)
            weatherPressureTextView.text = weatherDataFormatter.formatPressure(data.main?.pressure)
            weatherWindTextView.text = weatherDataFormatter.formatWind(data.wind?.speed, data.wind?.deg)
        }
    }

    private fun playShakeVideo() {
        YoYo.with(Techniques.Shake)
                .delay(SHAKE_ANIMATION_DELAY)
                .duration(SHAKE_ANIMATION_DURATION)
                .playOn(weatherMessageTextView)
    }
}
