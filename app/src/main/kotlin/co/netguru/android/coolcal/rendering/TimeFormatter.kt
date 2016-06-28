package co.netguru.android.coolcal.rendering

/**
 * Base interface for classes used to format timestamps into strings
 */
interface TimeFormatter {

    /**
     * Formats timestamp with provided pattern string
     * @param dt timestamp
     * @param pattern format pattern specification
     */
    open fun formatDateTime(dt: Long?, pattern: String): String?

    /**
     * Formats timestamp into time of day
     * @param dt timestamp
     * @param isAllDay is the event all-day. default is false
     */
    open fun formatTimeOfDay(dt: Long?, isAllDay: Boolean = false): String?

    /**
     * Formats timestamp into day of month
     * @param dt timestamp
     */
    open fun formatDayOfMonth(dt: Long?): String?

    /**
     * Formats timestamp into day of week name
     * @param dt timestamp
     */
    open fun formatDayOfWeek(dt: Long?): String?

    /**
     * Formats timestamp into day of week abbreviation
     * @param dt timestamp
     */
    open fun formatDayOfWeekShort(dt: Long?): String?

    /**
     * Formats provided timestamps of start and end of the period
     * @param dtStart start of the period
     * @param dtEnd end of the period
     */
    open fun formatPeriod(dtStart: Long?, dtEnd: Long?, isAllDay: Boolean = false): String?

    /**
     * Formats provided timestamp to a period string, with 0 acting as the start of the period
     * @param timeMillis timestamp
     * @param isAllDay is the event all-day. default is false
     */
    open fun formatPeriod(timeMillis: Long?, isAllDay: Boolean = false): String?

    /**
     * Format provided dates to a long date format, e.g. 20.06.2016, 8:00 - 9:00, 20.06.2016, All day,
     * 20.06.2016, 8:00 - 21.06.2016, 9:00.
     * @param dtStart First date in millis.
     * @param dtEnd Second date in millis.
     * @param isAllDay Value determinate if event is all day.
     */
    open fun formatLongDate(dtStart: Long?, dtEnd: Long?, isAllDay: Boolean = false): String?
}
