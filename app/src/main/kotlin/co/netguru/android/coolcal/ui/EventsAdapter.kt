package co.netguru.android.coolcal.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.model.Event
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.TimeUnit

class EventsAdapter : RecyclerView.Adapter<EventHolder>(),
        StickyRecyclerHeadersAdapter<HeaderHolder> {

    private var _events: List<Event> = ArrayList()

    var events: List<Event>
        get() = _events
        set(value) {
            _events = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventHolder? {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_event, parent, false)
        return EventHolder(view)
    }

    override fun onBindViewHolder(holder: EventHolder?, position: Int) {
        holder!!.bind(events[position])
    }

    // HEADERS

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): HeaderHolder? {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_header, parent, false)
        return HeaderHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: HeaderHolder?, position: Int) {
        val dt = DateTime(events[position].dtStart)
        val dateString = dt.toString("EEE dd", Locale.getDefault())
        holder!!.bind(dateString)
    }

    override fun getHeaderId(position: Int): Long {
        return TimeUnit.MILLISECONDS.toDays(events[position].dtStart);
    }
}

abstract class Holder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(obj: T)
}

class EventHolder(itemView: View) : Holder<Event>(itemView) {

    val titleTextView: TextView by lazy {
        itemView.findViewById(R.id.event_title) as TextView
    }

    override fun bind(obj: Event) {
        titleTextView.text = obj.title
    }

}

class HeaderHolder(itemView: View) : Holder<String>(itemView) {

    val dateTextView: TextView by lazy {
        itemView.findViewById(R.id.header_date) as TextView
    }

    override fun bind(obj: String) {
        dateTextView.text = obj
    }

}