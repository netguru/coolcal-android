package co.netguru.android.coolcal.calendar

import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable

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
 * @param owner event creator
 * @param location location of event(can be empty)
 * @param description additional notes for event(can be empty)
 */
data class Event(val id: Long,
                 val calendarId: Long,
                 val begin: Long,
                 val end: Long,
                 val isAllDay: Boolean = false,
                 val title: String? = null,
                 val displayColor: Int? = null,
                 val owner: String,
                 val location: String,
                 val description: String) : Parcelable {
    constructor(source: Parcel) : this(source.readLong(), source.readLong(), source.readLong()
            , source.readLong(), 1.toByte().equals(source.readByte())
            , source.readString(), source.readValue(Int::class.java.classLoader) as Int?, source.readString(), source.readString(), source.readString())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeLong(id)
        dest?.writeLong(calendarId)
        dest?.writeLong(begin)
        dest?.writeLong(end)
        dest?.writeByte((if (isAllDay) 1 else 0).toByte())
        dest?.writeString(title)
        dest?.writeValue(displayColor)
        dest?.writeString(owner)
        dest?.writeString(location)
        dest?.writeString(description)
    }

    companion object {
        @JvmField final val CREATOR: Parcelable.Creator<Event> = object : Parcelable.Creator<Event> {
            override fun createFromParcel(source: Parcel): Event {
                return Event(source)
            }

            override fun newArray(size: Int): Array<Event?> {
                return arrayOfNulls(size)
            }
        }

        @JvmStatic fun fromCursor(cursor: Cursor): Event = Event(
                cursor.eventId(),
                cursor.eventCalendarId(),
                cursor.eventBegin(),
                cursor.eventEnd(),
                cursor.eventIsAllDay(),
                cursor.eventTitle(),
                cursor.eventDisplayColor(),
                cursor.eventOwner(),
                cursor.eventLocation(),
                cursor.eventDescription())
    }
}