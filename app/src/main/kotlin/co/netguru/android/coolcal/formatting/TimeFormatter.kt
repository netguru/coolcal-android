package co.netguru.android.coolcal.formatting

interface TimeFormatter {

    open fun formatDateTime(dt: Long, pattern: String): String?

    open fun formatTimeOfDay(dt: Long): String?

    open fun formatDayOfMonth(dt: Long): String?

    open fun formatDayOfWeek(dt: Long): String?

    open fun formatDayOfWeekShort(dt: Long): String?

    open fun formatPeriod(dtStart: Long, dtEnd: Long): String?

}
