package co.netguru.android.coolcal.ui

import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.location.Location
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.calendar.Event
import co.netguru.android.coolcal.calendar.EventAdapter
import co.netguru.android.coolcal.calendar.Loaders
import co.netguru.android.coolcal.formatting.TimeFormatter
import co.netguru.android.coolcal.formatting.ValueFormatter
import co.netguru.android.coolcal.utils.logDebug
import co.netguru.android.coolcal.utils.logError
import co.netguru.android.coolcal.weather.OpenWeatherMap
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.view_calendar_today_summary.*
import org.joda.time.LocalDateTime
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EventsFragment : BaseFragment(), LoaderManager.LoaderCallbacks<Cursor>,
        SlidingUpPanelLayout.PanelSlideListener {

    init {
        App.component.inject(this)
    }

    @Inject lateinit var openWeatherMap: OpenWeatherMap
    @Inject lateinit var valueFormatter: ValueFormatter
    @Inject lateinit var timeFormatter: TimeFormatter

    private lateinit var adapter: EventAdapter

    private val interpolator = FastOutSlowInInterpolator()

    private val DAY_MILLIS = TimeUnit.DAYS.toMillis(1)
    private val todayDt: Long = LocalDateTime(System.currentTimeMillis())
            .toLocalDate().toDateTimeAtStartOfDay().millis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EventAdapter(context, null, 0)

        initEventsLoader()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayOfWeekTextView.text = timeFormatter.formatDayOfWeekShort(todayDt)
        dayOfMonthTextView.text = timeFormatter.formatDayOfMonth(todayDt)
        eventsCalendarTabView.days = (0..5).map { i -> todayDt + i * DAY_MILLIS }
        eventsListView.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).slidingLayout.setDragView(eventsCalendarTabView)
    }

    override fun onDestroy() {
        activity.supportLoaderManager.destroyLoader(Loaders.EVENT_LOADER)
        super.onDestroy()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        return when (id) {
            Event.ID -> CursorLoader(context,
                    Event.EVENTS_URI,
                    Event.EVENTS_PROJECTION,
                    Event.EVENTS_DTSTART_SELECTION,
                    arrayOf(args?.getLong(Event.ARG_DT_FROM).toString(),
                            args?.getLong(Event.ARG_DT_TO).toString()),
                    CalendarContract.Events.DTSTART)

            else -> null
        }
    }

    private fun switchActiveDay(firstVisibleItem: Int) {
        try {
            val dt = adapter.getItemDayStart(firstVisibleItem)
            eventsCalendarTabView.switchDay(dt)
        } catch(e: IllegalStateException) {
            logDebug { e.message }
        } catch (e: CursorIndexOutOfBoundsException) {
            logDebug { "CursorIndexOutOfBoundsException (Cursor returned from getItemDayStart)" }
        }
    }

    private fun initScrollListener() {
        switchActiveDay(eventsListView.firstVisiblePosition)
        eventsListView.setOnScrollListener(object : AbsListView.OnScrollListener {
            var firstVisibleCache = 0

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int,
                                  visibleItemCount: Int, totalItemCount: Int) {
                if (firstVisibleItem != firstVisibleCache) {
                    firstVisibleCache = firstVisibleItem
                    switchActiveDay(firstVisibleItem)
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                // nothing
            }
        })
    }

    private fun initTodayStatistics(data: Cursor?) {
        if (data != null) {
            val tomorrowDt = todayDt + TimeUnit.DAYS.toMillis(1)
            val range = todayDt..tomorrowDt
            var todayEvents = 0
            var busyTodaySum = 0L
            if (data.moveToFirst()) {
                while (data.moveToNext()) {
                    val dtStart = data.getLong(Event.Projection.DTSTART.ordinal)
                    val dtEnd = data.getLong(Event.Projection.DTEND.ordinal)
                    val isAllDay = data.getInt(Event.Projection.ALL_DAY.ordinal) != 0
                    if (isAllDay && (dtStart in range || dtEnd in range)) {
                        todayEvents += 1
                        busyTodaySum += dtEnd - dtStart
                    }
                }
            }
            data.moveToFirst()

            numberOfEventsTextView.text = "$todayEvents"
            busyForTextView.text = timeFormatter.formatPeriod(0, busyTodaySum)
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
        adapter.swapCursor(cursor)
        initTodayStatistics(cursor)
        initScrollListener()
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        // nic
    }

    private fun requestForecast(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        openWeatherMap.getForecast(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response ->
                    adapter.forecastResponse = response
                }, {
                    error ->
                    logError { error.message }
                    // todo: handle possible error - retry?
                })
    }

    private fun initEventsLoader() {
        val dtStop = todayDt + TimeUnit.DAYS.toMillis(5) // five days later
        val data = Bundle()
        data.putLong(Event.ARG_DT_FROM, todayDt)
        data.putLong(Event.ARG_DT_TO, dtStop)

        activity.supportLoaderManager.initLoader(Loaders.EVENT_LOADER, data, this)
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location != null) {
            requestForecast(location)
        }
    }

    private fun crossfadePanelAlpha(slideOffset: Float) {
        val offset = interpolator.getInterpolation(slideOffset)
        panelHandleLayout.alpha = 1f - offset
        eventsCalendarTabView.alpha = offset
    }

    override fun onPanelExpanded(panel: View?) {
        crossfadePanelAlpha(1f)
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        crossfadePanelAlpha(slideOffset)
    }

    override fun onPanelCollapsed(panel: View?) {
        crossfadePanelAlpha(0f)
    }

    override fun onPanelHidden(panel: View?) {
    }

    override fun onPanelAnchored(panel: View?) {
    }

    override fun onResume() {
        super.onResume()

        when ((activity as MainActivity).slidingLayout.panelState) {
            SlidingUpPanelLayout.PanelState.EXPANDED -> crossfadePanelAlpha(1f)
            else -> crossfadePanelAlpha(0f)
        }
    }
}
