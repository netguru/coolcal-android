package co.netguru.android.coolcal.calendar

import android.database.Cursor
import android.provider.CalendarContract

data class Event(val id: Long,
                 val title: String,
                 val dtStart: Long,
                 val dtStop: Long,
                 val displayColor: Int) {

    companion object {

        fun fromCursor(cursor: Cursor): Event {
            val id = cursor.getLong(Projection._ID.ordinal)
            val title = cursor.getString(Projection.TITLE.ordinal)
            val dtStart = cursor.getLong(Projection.DTSTART.ordinal)
            val dtEnd = cursor.getLong(Projection.DTEND.ordinal)
            val displayColor = cursor.getInt(Projection.DISPLAY_COLOR.ordinal)

            return Event(id, title, dtStart, dtEnd, displayColor)
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
                CalendarContract.Events.DISPLAY_COLOR)
    }

    enum class Projection {
        _ID,
        TITLE,
        DTSTART,
        DTEND,
        DISPLAY_COLOR
    }
}

data class TimelineData(val events: List<Event>,
                        val dtStart: Long,
                        val timeSpan: Long)