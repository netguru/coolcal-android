package co.netguru.android.coolcal.calendar

import android.database.Cursor
import android.provider.CalendarContract

data class Event(val id: Long,
                 val calendarId: Long,
                 val dtStart: Long,
                 val dtStop: Long,
        //                 val duration: Long,
                 val title: String?,
                 val calendarDisplayName: String?,
                 val displayColor: Int?,
                 val isAllDay: Boolean = false) {

    enum class Projection {
        _ID,
        TITLE,
        DTSTART,
        DTEND,
        DISPLAY_COLOR,
        CALENDAR_DISPLAY_NAME,
        CALENDAR_ID,
        //        DURATION,
        ALL_DAY
    }

    companion object {

        @JvmStatic fun fromCursor(cursor: Cursor): Event {
            val id = cursor.getLong(Projection._ID.ordinal)
            val title = cursor.getString(Projection.TITLE.ordinal)
            val dtStart = cursor.getLong(Projection.DTSTART.ordinal)
            val dtStop = cursor.getLong(Projection.DTEND.ordinal)
            val displayColor = cursor.getInt(Projection.DISPLAY_COLOR.ordinal)
            val calendarDisplayName = cursor.getString(Projection.CALENDAR_DISPLAY_NAME.ordinal)
            val calendarId = cursor.getLong(Projection.CALENDAR_ID.ordinal)
            //            val duration = cursor.getLong(Projection.DURATION.ordinal) // TODO: parse String
            val isAllDay = (cursor.getInt(Projection.ALL_DAY.ordinal) != 0)

            return Event(id,
                    calendarId,
                    dtStart,
                    dtStop,
                    //                    duration,
                    title,
                    calendarDisplayName,
                    displayColor,
                    isAllDay)
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
                CalendarContract.Events.DISPLAY_COLOR,
                CalendarContract.Events.CALENDAR_DISPLAY_NAME,
                CalendarContract.Events.CALENDAR_ID,
                //                CalendarContract.Events.DURATION,
                CalendarContract.Events.ALL_DAY)
    }
}

