package co.netguru.android.coolcal.ui

import android.database.Cursor
import android.location.Location
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.model.CursorRecyclerViewAdapter
import co.netguru.android.coolcal.model.Event
import co.netguru.android.coolcal.model.EventsAdapter
import co.netguru.android.coolcal.model.Loaders
import co.netguru.android.owm.api.OpenWeatherMap
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class EventsFragment : BaseFragment(),
        LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        const val TAG = "EventsFragment"
    }

    val recyclerView: RecyclerView by bindView(R.id.events_recyclerview)
    val adapter = EventsAdapter(null)
    var decorDataObserver: RecyclerView.AdapterDataObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEventsLoading()
    }

    private fun requestForecast(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        OpenWeatherMap.api.getForecast(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response ->
                    adapter.forecastResponse = response
                }, {
                    error -> // todo: handle possible error - retry?
                })
    }

    private fun initEventsLoading() {
        val now = System.currentTimeMillis()
        val weekLater = now + TimeUnit.DAYS.toMillis(5)
        val data = Bundle()
        data.putLong(Event.ARG_DT_FROM, now)
        data.putLong(Event.ARG_DT_TO, weekLater)

        activity.supportLoaderManager.initLoader(Loaders.EVENT_LOADER, data, this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.adapter = adapter
        val decor = StickyRecyclerHeadersDecoration(adapter)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(decor)

        decorDataObserver = DecorAdapterDataObserver(WeakReference(decor))
        adapter.registerAdapterDataObserver(decorDataObserver)
    }

    override fun onDestroy() {
        if (decorDataObserver != null) {
            adapter.unregisterAdapterDataObserver(decorDataObserver)
        }
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
        adapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        // nic
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location != null) {
            requestForecast(location)
        }
    }

    class DecorAdapterDataObserver(val decorRef: WeakReference<StickyRecyclerHeadersDecoration>)
    : RecyclerView.AdapterDataObserver() {

        override fun onChanged() {
            decorRef.get()?.invalidateHeaders()
        }
    }
}
