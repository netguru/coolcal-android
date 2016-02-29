package co.netguru.android.coolcal.calendar

import android.database.Cursor

/**
 * Calendar event instance model
 *
 * @param id id of the instance
 * @param calendarId id of the calendar the instance belongs to
 * @param begin event instance begin time in UTC millis
 * @param end event instance end time in UTC millis
 * @param isAllDay is the event instance all-day or not
 * @param title title of the event instance (can be null)
 * @param displayColor display color of the event instance (can be null)
 */
data class Event(val id: Long,
                 val calendarId: Long,
                 val begin: Long,
                 val end: Long,
                 val isAllDay: Boolean = false,
                 val title: String?,
                 val displayColor: Int?) {

    companion object {

        @JvmStatic fun fromCursor(cursor: Cursor): Event = Event(
                cursor.eventId(),
                cursor.eventCalendarId(),
                cursor.eventBegin(),
                cursor.eventEnd(),
                cursor.eventIsAllDay(),
                cursor.eventTitle(),
                cursor.eventDisplayColor())
    }
}