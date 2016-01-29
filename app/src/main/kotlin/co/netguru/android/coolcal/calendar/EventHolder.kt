package co.netguru.android.coolcal.calendar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.utils.AppPreferences
import co.netguru.android.coolcal.weather.Forecast
import co.netguru.android.coolcal.weather.WeatherDecoder
import com.twotoasters.sectioncursoradapter.adapter.viewholder.ViewHolder
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import org.joda.time.format.PeriodFormatterBuilder

class EventHolder(itemView: View) : ViewHolder(itemView) {

    companion object {
        private val periodFormatter: PeriodFormatter by lazy {
            PeriodFormatterBuilder()
                    .appendHours()
                    .appendSuffix("h")
                    .appendSeparator(" ")
                    .appendMinutes()
                    .appendSuffix("m")
                    .toFormatter()
        }
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
        titleTextView.text = obj.title
        timeTextView.text = AppPreferences.formatTime(obj.dtStart)
        messageTextView.text = "not implemented yet" //todo
        durationTextView.text = Period(obj.dtStart, obj.dtStop).toString(periodFormatter)
        if (forecast != null) {
            temperatureTextView.visibility = View.VISIBLE
            weatherIconImageView.visibility = View.VISIBLE
            temperatureTextView.text = AppPreferences.formatTemperature(forecast.main?.temperature)
            weatherIconImageView.setImageResource(WeatherDecoder.getIconRes(forecast.weatherList?.get(0).icon))
        } else {
            temperatureTextView.visibility = View.GONE
            weatherIconImageView.visibility = View.GONE
        }
    }

}