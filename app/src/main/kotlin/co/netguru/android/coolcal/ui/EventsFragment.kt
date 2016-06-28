package co.netguru.android.coolcal.ui

import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import android.location.Location
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.calendar.*
import co.netguru.android.coolcal.preferences.AppPreferences
import co.netguru.android.coolcal.rendering.TimeFormatter
import co.netguru.android.coolcal.rendering.WeatherDataFormatter
import co.netguru.android.coolcal.utils.logError
import co.netguru.android.coolcal.utils.updateNeeded
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

class EventsFragment : BaseFragment(), SlidingUpPanelLayout.PanelSlideListener, EventHolder.EventHolderListener {

    companion object {

        private val EVENTS_LOADER = 0
        private val TODAY_EVENTS_LOADER = 1

        // loader id's
        val loaders = intArrayOf(EVENTS_LOADER, TODAY_EVENTS_LOADER)

        private val DAY_MILLIS = TimeUnit.DAYS.toMillis(1)
    }

    init {
        App.component.inject(this)
    }

    @Inject lateinit var openWeatherMap: OpenWeatherMap
    @Inject lateinit var weatherDataFormatter: WeatherDataFormatter
    @Inject lateinit var timeFormatter: TimeFormatter
    @Inject lateinit var appPreferences: AppPreferences

    private lateinit var adapter: EventAdapter

    private val interpolator = FastOutSlowInInterpolator()
    private val todayDt: Long = LocalDateTime(System.currentTimeMillis())
            .toLocalDate().toDateTimeAtStartOfDay().millis

    private val cursorLoaderCallback = InstancesLoaderCallbacks()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = EventAdapter(context, null, 0, this)
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
        eventsCalendarTabView.dayClickListener = object : CalendarTabView.OnDayClickListener {
            override fun onDayClick(dayInMillis: Long) {
                val pos = adapter.findSectionPosition(dayInMillis)
                if (pos != null) {
                    eventsListView.smoothScrollToPosition(pos)
                }
            }
        }
        eventsListView.emptyView = eventsListEmptyView
        eventsListView.adapter = adapter

        adapter.forecastResponse = appPreferences.lastForecast
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).slidingLayout.setDragView(eventsCalendarTabView)
    }

    fun onCalendarPermissionGranted() {
        initEventsLoader(cursorLoaderCallback)
    }

    override fun OnEventCLick(clickedEvent: Event) {
        EventDetailsActivity.newIntent(activity, clickedEvent)
    }

    private fun initEventsLoader(callbacks: LoaderManager.LoaderCallbacks<Cursor>) {
        loaders.forEach {
            when (it) {
                EVENTS_LOADER -> {
                    restartEventsLoader(EVENTS_LOADER, 0, 5, callbacks)
                }
                TODAY_EVENTS_LOADER -> {
                    restartEventsLoader(TODAY_EVENTS_LOADER, 0, 1, callbacks)
                }
                else -> {
                    throw IllegalArgumentException("Unknown loader id: $it")
                }

            }
        }
    }

    private fun restartEventsLoader(loaderId: Int, dtStartNumber: Int, dtEndNumber: Int, callbacks: LoaderManager.LoaderCallbacks<Cursor>) {
        val dtStart = todayDt + dtStartNumber * DAY_MILLIS
        val dtStop = todayDt + dtEndNumber * DAY_MILLIS
        val data = Bundle()
        data.putLong(InstancesLoader.ARG_DT_FROM, dtStart)
        data.putLong(InstancesLoader.ARG_DT_TO, dtStop)

        activity.supportLoaderManager.restartLoader(loaderId, data, callbacks)
    }

    private fun switchActiveDay(firstVisibleItem: Int) {
        try {
            val dt = adapter.getItemDayStart(firstVisibleItem)
            eventsCalendarTabView.switchDay(dt)
        } catch(e: IllegalStateException) {
            logError(e.message)
        } catch (e: CursorIndexOutOfBoundsException) {
            logError("CursorIndexOutOfBoundsException (Cursor returned from getItemDayStart)")
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

    private fun initTodayStatistics(cursor: Cursor) {
        var todayEvents = 0
        var busyTodaySum = 0L
        if (cursor.moveToFirst()) {
            do {
                val duration = cursor.eventDuration()
                val isAllDay = cursor.eventIsAllDay()
                if (!isAllDay) {
                    todayEvents += 1
                    busyTodaySum += duration
                }
            } while (cursor.moveToNext())
        }
        cursor.close()

        numberOfEventsTextView.text = "$todayEvents"
        busyForTextView.text = timeFormatter.formatPeriod(busyTodaySum)
    }

    private fun requestForecast(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        openWeatherMap.getForecast(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response ->
                    appPreferences.lastForecast = response
                    adapter.forecastResponse = response
                }, {
                    error ->
                    logError(error.message)
                    // todo: handle possible error - retry?
                })
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location != null && updateNeeded(appPreferences.lastWeatherSync)) {
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
        eventsCalendarTabView.areClickableDays(true)
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        crossfadePanelAlpha(slideOffset)
    }

    override fun onPanelCollapsed(panel: View?) {
        crossfadePanelAlpha(0f)
        eventsCalendarTabView.areClickableDays(false)
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

    inner class InstancesLoaderCallbacks :
            LoaderManager.LoaderCallbacks<Cursor> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
            when (id) {
                in loaders -> {
                    val dtTo = args!!.getLong(InstancesLoader.ARG_DT_TO).toString()
                    val dtFrom = args.getLong(InstancesLoader.ARG_DT_FROM).toString()

                    return InstancesLoader.createLoader(context, dtFrom, dtTo)
                }

                else -> return null
            }
        }

        override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
            synchronized(this) {
                val id = loader?.id!!
                when (id) {
                    EVENTS_LOADER -> {
                        adapter.swapCursor(cursor)
                        initScrollListener()
                    }
                    TODAY_EVENTS_LOADER -> {
                        initTodayStatistics(cursor!!)
                    }
                    else -> {
                        throw IllegalArgumentException("Unknown loader id : $id")
                    }
                }
            }
        }

        override fun onLoaderReset(loader: Loader<Cursor>?) {
            adapter.swapCursor(null)
        }
    }
}
