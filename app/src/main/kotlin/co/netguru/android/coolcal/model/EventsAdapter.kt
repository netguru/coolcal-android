package co.netguru.android.coolcal.model

import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.netguru.android.coolcal.R
import co.netguru.android.owm.api.Forecast
import co.netguru.android.owm.api.ForecastResponse
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.TimeUnit

class EventsAdapter(cursor: Cursor?) :
        HeaderCursorRecyclerViewAdapter<EventsAdapter.EventHolder,
                EventsAdapter.HeaderHolder>(cursor) {

    companion object {
        const val TAG = "EventsAdapter"
    }

    private var _forecastResponse: ForecastResponse? = null
    var forecastResponse: ForecastResponse?
        get() = _forecastResponse
        set(value) {
            _forecastResponse = value
            notifyDataSetChanged()
        }

    /*
        Events
     */

    override fun onBindViewHolder(viewHolder: EventHolder?, cursor: Cursor?) {
        val event = Event.fromCursor(cursor!!)
        val eventWeather = forecastResponse?.forecastList?.filter {
            forecast ->
                event.dtStart in forecast.range3h()
        }?.lastOrNull()

        viewHolder!!.bind(event, eventWeather)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventHolder? {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_event, parent, false)
        return EventHolder(view)
    }

    /*
        Headers
     */

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): HeaderHolder? {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_header, parent, false)
        return HeaderHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: HeaderHolder, cursor: Cursor) {
        val dtStart = cursor.getLong(Event.Projection.DTSTART.ordinal)
        val dateString = DateTime(dtStart).toString("EEE dd", Locale.getDefault())
        holder.bind(dateString)
    }

    override fun getHeaderId(cursor: Cursor): Long {
        val dtStart = cursor.getLong(Event.Projection.DTSTART.ordinal)
        return TimeUnit.MILLISECONDS.toDays(dtStart)
    }

    class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView by lazy {
            itemView.findViewById(R.id.event_title) as TextView
        }

        fun bind(obj: Event, forecast: Forecast?) {
            titleTextView.text = "${obj.title} : ${forecast?.weatherList?.get(0)?.description}"
        }

    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dateTextView: TextView by lazy {
            itemView.findViewById(R.id.header_date) as TextView
        }

        fun bind(obj: String) {
            dateTextView.text = obj
        }

    }
}
