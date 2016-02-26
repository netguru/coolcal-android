package co.netguru.android.coolcal.rendering

import android.content.Context
import co.netguru.android.coolcal.R
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatter
import java.util.*

class TimeFormatterImpl(val context: Context, val locale: Locale, val periodFormatter: PeriodFormatter) : TimeFormatter {

    override fun formatDateTime(dt: Long?, pattern: String) = when (dt) {
        is Long -> DateTime(dt).toLocalDateTime().toString(pattern, locale)
        else -> ""
    }

    override fun formatTimeOfDay(dt: Long?, isAllDay: Boolean) = when (isAllDay) {
        true -> context.getString(R.string.time_placeholder)
        false -> formatDateTime(dt, "H:mm")
    }

    override fun formatDayOfMonth(dt: Long?) = formatDateTime(dt, "dd")

    override fun formatDayOfWeek(dt: Long?) = formatDateTime(dt, "EEEE")

    override fun formatDayOfWeekShort(dt: Long?) = formatDateTime(dt, "EEE")

    override fun formatPeriod(dtStart: Long?, dtEnd: Long?, isAllDay: Boolean) = when (isAllDay) {
        false -> if (dtStart != null && dtEnd != null)
            Period(dtStart, dtEnd).toString(periodFormatter) else ""
        true -> context.getString(R.string.all_day)
    }

    override fun formatPeriod(timeMillis: Long?, isAllDay: Boolean): String? =
            formatPeriod(0, timeMillis)

}