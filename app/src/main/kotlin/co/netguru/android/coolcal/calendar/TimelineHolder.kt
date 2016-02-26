package co.netguru.android.coolcal.calendar

import android.view.View
import android.widget.TextView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.rendering.TimeFormatter
import com.twotoasters.sectioncursoradapter.adapter.viewholder.ViewHolder
import javax.inject.Inject

class TimelineHolder(itemView: View) : ViewHolder(itemView) {

    init {
        App.component.inject(this)
    }

    @Inject lateinit var timeFormatter: TimeFormatter

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

        dayOfMonthTextView.text = timeFormatter.formatDayOfMonth(obj.dtStart)
        dayOfWeekTextView.text = timeFormatter.formatDayOfWeek(obj.dtStart)
        eventTimelineView.refresh {
            adapter = EventTimelineAdapter(obj)
            timelineDtStart = obj.dtStart
            timeSpan = obj.timeSpan
        }
    }
}

