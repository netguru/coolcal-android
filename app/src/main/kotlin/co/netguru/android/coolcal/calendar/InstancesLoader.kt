package co.netguru.android.coolcal.calendar

import android.content.Context
import android.provider.CalendarContract
import android.support.v4.content.CursorLoader

/**
 * Definitions for loading CalendarContract.Instances
 */
object InstancesLoader {

    @JvmStatic val INSTANCES_URI = CalendarContract.Instances.CONTENT_URI

    // Args for bundle
    const val ARG_DT_FROM = "begin_from"
    const val ARG_DT_TO = "end_to"

    val selection = "BEGIN >= ? AND BEGIN < ?"

    /**
     * Creates a CursorLoader for the CalendarContract.Instances provider
     * with specified period of events, sorted by begin time of event
     * @param from period start millis UTC
     * @param to period stop millis UTC
     */
    fun createLoader(context: Context, from: String, to: String) =
            CursorLoader(context,
                    buildUri(from, to),
                    INSTANCES_PROJECTION,
                    selection,
                    arrayOf(from, to),
                    "${CalendarContract.Instances.BEGIN} ASC") // sort chronologically

    /**
     * Builds the CalendarContract.Instances.CONTENT_URI for a given time period
     * @param from period start millis UTC converted to String
     * @param to period stop millis UTC converted to String
     */
    fun buildUri(from: String, to: String) = INSTANCES_URI
            .buildUpon()
            .appendPath(from)
            .appendPath(to)
            .build()

    // Projection for querying instances
    val INSTANCES_PROJECTION = arrayOf(
            CalendarContract.Instances._ID,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.END,
            CalendarContract.Instances.DISPLAY_COLOR,
            CalendarContract.Instances.CALENDAR_DISPLAY_NAME,
            CalendarContract.Instances.CALENDAR_ID,
            CalendarContract.Instances.DURATION,
            CalendarContract.Instances.ALL_DAY)
}

