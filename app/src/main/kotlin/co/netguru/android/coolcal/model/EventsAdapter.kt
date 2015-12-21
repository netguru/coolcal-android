package co.netguru.android.coolcal.model

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.netguru.android.coolcal.R
import org.joda.time.DateTime
import java.util.*
import java.util.concurrent.TimeUnit

class EventsAdapter(context: Context, cursor: Cursor?) :
        HeaderCursorRecyclerViewAdapter<EventsAdapter.EventHolder,
                EventsAdapter.HeaderHolder>(context, cursor) {

    /*
        Events
     */

    override fun onBindViewHolder(viewHolder: EventHolder?, cursor: Cursor?) {
        val event = Event.fromCursor(cursor!!)
        viewHolder!!.bind(event)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventHolder? {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_event, parent, false)
        return EventHolder(view)
    }

    /*
        Headers
     */

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): HeaderHolder? {
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_header, parent, false)
        return HeaderHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: HeaderHolder, cursor: Cursor) {
        val dtStart = cursor.getLong(Event.Projection.DTSTART.ordinal)
        val dateString = DateTime(dtStart).toString("EEE dd", Locale.getDefault())
        holder.bind(dateString)
    }

    override fun getHeaderId(cursor: Cursor): Long {
        val dtStart = cursor.getLong(Event.Projection.DTSTART.ordinal)
        return TimeUnit.MILLISECONDS.toDays(dtStart)
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
}
