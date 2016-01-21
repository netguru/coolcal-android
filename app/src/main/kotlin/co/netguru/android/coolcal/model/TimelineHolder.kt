package co.netguru.android.coolcal.model

import android.view.View
import android.widget.TextView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.ui.EventTimelineView
import com.twotoasters.sectioncursoradapter.adapter.viewholder.ViewHolder
import org.joda.time.DateTime

class TimelineHolder(itemView: View) : ViewHolder(itemView) {

    val eventTimelineView: EventTimelineView by lazy {
        itemView.findViewById(R.id.event_timeline_view) as EventTimelineView
    }

    val dateTextView: TextView by lazy {
        itemView.findViewById(R.id.event_section_date) as TextView
    }

    fun bind(obj: TimelineData) {
        dateTextView.text = DateTime(obj.startDt).toString("dd EEEE")
        eventTimelineView.events = obj.events
        eventTimelineView.startDt = obj.startDt
        eventTimelineView.timeSpan = obj.timeSpan
    }

}

