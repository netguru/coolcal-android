package co.netguru.android.coolcal.calendar

import android.database.Cursor
import android.location.Location
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import butterknife.bindView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.BaseFragment
import co.netguru.android.coolcal.utils.Loaders
import co.netguru.android.coolcal.weather.OpenWeatherMap
import org.joda.time.LocalDateTime
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class EventsFragment : BaseFragment(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        const val TAG = "EventsFragment"
        val DAY_MILLIS = TimeUnit.DAYS.toMillis(1)
    }

    val listView: ListView by bindView(R.id.events_listview)
    val calendarTabView: CalendarTabView by bindView(R.id.events_calendar_tab_view)
    var adapter: EventAdapter? = null

    val dtStart: Long = LocalDateTime(System.currentTimeMillis())
            .toLocalDate().toDateTimeAtStartOfDay().millis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EventAdapter(context, null, 0)
        initEventsLoading()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarTabView.days = (0..5).map { i -> dtStart + i * DAY_MILLIS }
        listView.adapter = adapter
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            var firstVisibleCache = 0

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int,
                                  visibleItemCount: Int, totalItemCount: Int) {
                if (firstVisibleItem != firstVisibleCache) {
                    firstVisibleCache = firstVisibleItem
                    val dt = adapter!!.getItemDayStart(firstVisibleItem)
                    calendarTabView.switchDay(dt)
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                // nothing
            }
        })
    }

    override fun onDestroy() {
        activity.supportLoaderManager.destroyLoader(Loaders.EVENT_LOADER)
        super.onDestroy()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        val selectionArgs = arrayOf(
                args?.getLong(Event.ARG_DT_FROM).toString(),
                args?.getLong(Event.ARG_DT_TO).toString())
        return when (id) {
            Event.ID -> CursorLoader(context,
                    Event.EVENTS_URI,
                    Event.EVENTS_PROJECTION,
                    Event.EVENTS_DTSTART_SELECTION,
                    selectionArgs,
                    CalendarContract.Events.DTSTART)

            else -> null
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        adapter?.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        // nic
    }

    private fun requestForecast(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        OpenWeatherMap.api.getForecast(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response ->
                    adapter?.forecastResponse = response
                }, {
                    error -> // todo: handle possible error - retry?
                })
    }

    private fun initEventsLoading() {
        val dtStop = dtStart + TimeUnit.DAYS.toMillis(5) // five days later
        val data = Bundle()
        data.putLong(Event.ARG_DT_FROM, dtStart)
        data.putLong(Event.ARG_DT_TO, dtStop)

        activity.supportLoaderManager.initLoader(Loaders.EVENT_LOADER, data, this)
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location != null) {
            requestForecast(location)
        }
    }
}
