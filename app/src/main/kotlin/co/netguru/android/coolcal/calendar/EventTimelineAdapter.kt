package co.netguru.android.coolcal.calendar

class EventTimelineAdapter(val data: TimelineData) : EventTimelineView.Adapter {

    override fun isItemAllDay(position: Int) = data.events[position].isAllDay

    override fun getItemDateStart(position: Int) = data.events[position].dtStart

    override fun getItemDateStop(position: Int) = data.events[position].dtStop

    override fun getItemColor(position: Int) = data.events[position].displayColor

    override fun getItemTitle(position: Int) = data.events[position].title ?: ""

    override fun getItemCount(): Int = data.events.size
}
