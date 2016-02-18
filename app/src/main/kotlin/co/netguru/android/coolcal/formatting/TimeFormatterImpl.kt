package co.netguru.android.coolcal.formatting

import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import java.util.*

class TimeFormatterImpl(val locale: Locale, val periodFormatter: PeriodFormatter) : TimeFormatter {

    override fun formatDateTime(dt: Long, pattern: String) =
            DateTime(dt).toLocalDateTime().toString(pattern, locale)

    override fun formatTimeOfDay(dt: Long) = formatDateTime(dt, "H:mm")

    override fun formatDayOfMonth(dt: Long) = formatDateTime(dt, "dd")

    override fun formatDayOfWeek(dt: Long) = formatDateTime(dt, "EEEE")

    override fun formatDayOfWeekShort(dt: Long) = formatDateTime(dt, "EEE")

    override fun formatPeriod(dtStart: Long, dtEnd: Long) =
            Period(dtStart, dtEnd).toString(periodFormatter)
}