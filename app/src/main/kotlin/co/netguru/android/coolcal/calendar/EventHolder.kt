package co.netguru.android.coolcal.calendar

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.rendering.TimeFormatter
import co.netguru.android.coolcal.rendering.WeatherDataFormatter
import co.netguru.android.coolcal.rendering.WeatherDecoder
import co.netguru.android.coolcal.rendering.WeatherDescriptionHelper
import co.netguru.android.coolcal.weather.Forecast
import com.twotoasters.sectioncursoradapter.adapter.viewholder.ViewHolder
import org.joda.time.DateTime
import javax.inject.Inject

class EventHolder(itemView: View) : ViewHolder(itemView) {

    init {
        App.component.inject(this)
    }

    @Inject lateinit var weatherDecoder: WeatherDecoder
    @Inject lateinit var weatherDataFormatter: WeatherDataFormatter
    @Inject lateinit var timeFormatter: TimeFormatter
    @Inject lateinit var resources: Resources
    @Inject lateinit var weatherDescription: WeatherDescriptionHelper

    val time_Indicator: View by lazy {
        itemView.findViewById(R.id.event_time_indicator)
    }
    val titleTextView: TextView by lazy {
        itemView.findViewById(R.id.event_title) as TextView
    }
    val timeTextView: TextView by lazy {
        itemView.findViewById(R.id.event_time) as TextView
    }
    val durationTextView: TextView by lazy {
        itemView.findViewById(R.id.event_duration) as TextView
    }
    val messageTextView: TextView by lazy {
        itemView.findViewById(R.id.event_message) as TextView
    }
    val temperatureTextView: TextView by lazy {
        itemView.findViewById(R.id.event_temperature) as TextView
    }
    val weatherIconImageView: ImageView by lazy {
        itemView.findViewById(R.id.event_weather_icon) as ImageView
    }

    fun bind(obj: Event, forecast: Forecast?) {
        titleTextView.text = obj.title ?: ""
        timeTextView.text = timeFormatter.formatTimeOfDay(obj.begin, obj.isAllDay)
        durationTextView.text = timeFormatter.formatPeriod(obj.begin, obj.end, obj.isAllDay)
        if (forecast == null || obj.isAllDay) {
            temperatureTextView.visibility = View.GONE
            weatherIconImageView.visibility = View.GONE
        } else {
            val temperature = forecast.main?.temperature
            val icon = forecast.weatherList[0].icon
            temperatureTextView.visibility = View.VISIBLE
            weatherIconImageView.visibility = View.VISIBLE
            temperatureTextView.text = weatherDataFormatter.formatTemperature(temperature)
            weatherIconImageView.setImageResource(weatherDecoder.getIconRes(icon))
            messageTextView.text = weatherDescription.getDescription(icon, temperature,
                    forecast.main?.pressure)
        }
        if (isCurrentEvent(obj.begin, obj.end) && !obj.isAllDay) {
            time_Indicator.visibility = View.VISIBLE
        } else {
            time_Indicator.visibility = View.GONE
        }
    }

    private fun isCurrentEvent(begin: Long, end: Long): Boolean {
        var currentTime = DateTime(System.currentTimeMillis()).toLocalDateTime()
        return DateTime(begin).toLocalDateTime().isBefore(currentTime)
                && DateTime(end).toLocalDateTime().isAfter(currentTime)
    }
}