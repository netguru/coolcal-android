package co.netguru.android.coolcal.model

import android.database.Cursor
import android.provider.CalendarContract

data class Event(val id: Long,
                 val title: String,
                 val dtStart: Long,
                 val dtStop: Long) {

    companion object {

        fun fromCursor(cursor: Cursor): Event {
            val id = cursor.getLong(Projection._ID.ordinal)
            val title = cursor.getString(Projection.TITLE.ordinal)
            val dtStart = cursor.getLong(Projection.DTSTART.ordinal)
            val dtEnd = cursor.getLong(Projection.DTEND.ordinal)

            return Event(id, title, dtStart, dtEnd)
        }

        const val ARG_DT_FROM = "dt_from"
        const val ARG_DT_TO = "dt_to"
        const val ID = 0
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
}