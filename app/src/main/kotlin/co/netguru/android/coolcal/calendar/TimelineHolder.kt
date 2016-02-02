package co.netguru.android.coolcal.calendar

import android.view.View
import android.widget.TextView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.utils.AppPreferences
import com.twotoasters.sectioncursoradapter.adapter.viewholder.ViewHolder

class TimelineHolder(itemView: View) : ViewHolder(itemView) {

    val eventTimelineView: EventTimelineView by lazy {
        itemView.findViewById(R.id.event_timeline_view) as EventTimelineView
    }

    val dayOfMonthTextView: TextView by lazy {
        itemView.findViewById(R.id.event_header_day_of_month) as TextView
    }

    val dayOfWeekTextView: TextView by lazy {
        itemView.findViewById(R.id.event_header_day_of_week) as TextView
    }

    fun bind(obj: TimelineData) {
        dayOfMonthTextView.text = AppPreferences.formatDayOfMonth(obj.dtStart)
        dayOfWeekTextView.text = AppPreferences.formatDayOfWeek(obj.dtStart)
        eventTimelineView.events = obj.events
        eventTimelineView.startDt = obj.dtStart
        eventTimelineView.timeSpan = obj.timeSpan
    }

}

