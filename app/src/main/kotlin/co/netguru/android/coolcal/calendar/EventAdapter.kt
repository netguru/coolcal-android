package co.netguru.android.coolcal.calendar

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.View
import android.view.ViewGroup
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.weather.ForecastResponse
import com.twotoasters.sectioncursoradapter.adapter.SectionCursorAdapter
import org.joda.time.LocalDateTime
import java.util.concurrent.TimeUnit

class EventAdapter(context: Context, cursor: Cursor?, flags: Int) :
        SectionCursorAdapter<TimelineData, TimelineHolder, EventHolder>
        (context, cursor, flags, R.layout.item_timeline, R.layout.item_event) {

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

    override fun bindItemViewHolder(viewHolder: EventHolder?, cursor: Cursor?, parent: ViewGroup?) {
        val event = Event.fromCursor(cursor!!)
        val eventWeather = forecastResponse?.forecastList?.filter { forecast ->
            event.dtStart in forecast.range3h()
        }?.lastOrNull()

        viewHolder!!.bind(event, eventWeather)
    }

    override fun getSectionFromCursor(cursor: Cursor?): TimelineData? {
        val startPos = cursor!!.position
        val list = arrayListOf<Event>()
        val origDt = getEventDayStart(cursor)
        val timeSpan = TimeUnit.DAYS.toMillis(1)
        // todo: optimize - go left & right (assume sorted)
        if (cursor.moveToFirst()) {
            do {
                val refDt = getEventDayStart(cursor)
                if (refDt == origDt) {
                    list.add(Event.fromCursor(cursor))
                }
            } while (cursor.moveToNext())
        }
        // haxy pod sectioncursoradapter
        cursor.moveToPosition(startPos)
        return TimelineData(list, origDt, timeSpan)
    }

    override fun createItemViewHolder(cursor: Cursor?, itemView: View?): EventHolder?
            = EventHolder(itemView!!)

    override fun createSectionViewHolder(sectionView: View?, section: TimelineData?): TimelineHolder?
            = TimelineHolder(sectionView!!)

    override fun bindSectionViewHolder(position: Int, sectionViewHolder: TimelineHolder?,
                                       parent: ViewGroup?, section: TimelineData?) {
        Log.i(TAG, "Binding section holder with ${section.toString()}")
        sectionViewHolder!!.bind(section!!)
    }

    private fun getEventDayStart(cursor: Cursor): Long {
        val dt = Event.Projection.DTSTART.ordinal
        return LocalDateTime(cursor.getLong(dt)).toLocalDate().toDateTimeAtStartOfDay().millis
    }
}