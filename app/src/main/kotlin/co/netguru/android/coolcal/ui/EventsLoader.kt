package co.netguru.android.coolcal.ui

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import co.netguru.android.coolcal.model.Event
import java.util.*

class EventsLoader(val context: Context, val eventsListListener: EventsLoader.EventsListListener) :
        LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        val selectionArgs = arrayOf(
                args?.getLong(ARG_DT_FROM).toString(),
                args?.getLong(ARG_DT_TO).toString())
        return when (id) {
            ID -> CursorLoader(context,
                    EVENTS_URI,
                    EVENTS_PROJECTION,
                    EVENTS_DTSTART_SELECTION,
                    selectionArgs,
                    CalendarContract.Events.DTSTART)

            else -> null
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        val events = ArrayList<Event>()
        while(data!!.moveToNext()) {
            val id = data.getLong(Projection._ID.ordinal)
            val title = data.getString(Projection.TITLE.ordinal)
            val dtStart = data.getLong(Projection.DTSTART.ordinal)
            val dtEnd = data.getLong(Projection.DTEND.ordinal)

            events.add(Event(id, title, dtStart, dtEnd))
        }

        eventsListListener.onLoad(events)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        eventsListListener.onReset()
    }

    companion object {
        val ARG_DT_FROM = "dt_from"
        val ARG_DT_TO = "dt_to"
        val ID = 0
        val EVENTS_URI = CalendarContract.Events.CONTENT_URI;
        val EVENTS_DTSTART_SELECTION =
                "${CalendarContract.Events.DTSTART} >= ? AND " +
                        "${CalendarContract.Events.DTSTART} < ?"
        val EVENTS_PROJECTION = arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_TIMEZONE)
    }

    enum class Projection {
        _ID,
        TITLE,
        DTSTART,
        DTEND,
        EVENT_TIMEZONE
    }

    interface EventsListListener {
        fun onLoad(events: List<Event>)
        fun onReset()
    }
}

