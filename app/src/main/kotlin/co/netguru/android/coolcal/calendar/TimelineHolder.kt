package co.netguru.android.coolcal.calendar

import android.view.View
import android.widget.TextView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.preferences.AppPreferences
import com.twotoasters.sectioncursoradapter.adapter.viewholder.ViewHolder

class TimelineHolder(itemView: View) : ViewHolder(itemView) {

    val eventTimelineView: EventTimelineView by lazy {
        itemView.findViewById(R.id.eventTimelineView) as EventTimelineView
    }

    val dayOfMonthTextView: TextView by lazy {
        itemView.findViewById(R.id.dayOfMonthTextView) as TextView
    }

    val dayOfWeekTextView: TextView by lazy {
        itemView.findViewById(R.id.dayOfWeekTextView) as TextView
    }

    fun bind(obj: TimelineData) {

        dayOfMonthTextView.text = AppPreferences.formatDayOfMonth(obj.dtStart)
        dayOfWeekTextView.text = AppPreferences.formatDayOfWeek(obj.dtStart)
        eventTimelineView invalidating {
            events = obj.events
            startDt = obj.dtStart
            timeSpan = obj.timeSpan
        }
    }
}

