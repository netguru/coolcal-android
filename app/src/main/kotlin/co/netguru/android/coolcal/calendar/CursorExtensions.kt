package co.netguru.android.coolcal.calendar

import android.database.Cursor
import android.provider.CalendarContract.Instances.*


inline fun <T> Cursor.from(columnName: String, getter: Cursor.(Int) -> T): T {
    val columnIndex = getColumnIndex(columnName)
    return getter(columnIndex)
}

fun Cursor.eventId() = from(_ID) { getLong(it) }
fun Cursor.eventCalendarId() = from(CALENDAR_ID) { getLong(it) }
fun Cursor.eventBegin() = from(BEGIN) { getLong(it) }
fun Cursor.eventEnd() = from(END) { getLong(it) }
fun Cursor.eventIsAllDay() = from(ALL_DAY) { getInt(it) } != 0
fun Cursor.eventTitle() = from(TITLE) { getString(it) }
fun Cursor.eventDisplayColor() = from(DISPLAY_COLOR) { getInt(it) }
fun Cursor.eventDuration() = eventEnd() - eventBegin()

