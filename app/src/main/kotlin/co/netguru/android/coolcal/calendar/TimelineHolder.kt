package co.netguru.android.coolcal.calendar

import android.view.View
import android.widget.TextView
import co.netguru.android.coolcal.R
import com.twotoasters.sectioncursoradapter.adapter.viewholder.ViewHolder
import org.joda.time.DateTime

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
        dayOfMonthTextView.text = DateTime(obj.startDt).toLocalDateTime().toString("dd")
        dayOfWeekTextView.text = DateTime(obj.startDt).toLocalDateTime().toString("EEEE")
        eventTimelineView.events = obj.events
        eventTimelineView.startDt = obj.startDt
        eventTimelineView.timeSpan = obj.timeSpan
    }

}

