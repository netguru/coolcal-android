package co.netguru.android.coolcal.ui

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.Fragment
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
import co.netguru.android.coolcal.model.Event
import co.netguru.android.coolcal.model.EventsAdapter
import co.netguru.android.coolcal.model.Loaders
import co.netguru.android.owm.api.OpenWeatherMap
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class EventsFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    val recyclerView: RecyclerView by bindView(R.id.events_recyclerview)
    var adapter: EventsAdapter? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        adapter = EventsAdapter(context!!, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initForecastRequest()
        initEventsLoading()
    }

    private fun initForecastRequest() {
        OpenWeatherMap.api.getForecast(city = "Kraków")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response -> adapter?.forecastResponse = response
                }, {
                    error -> // todo: handle error
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
        recyclerView.layoutManager = LinearLayoutManager(context)
        val decor = StickyRecyclerHeadersDecoration(adapter)
        recyclerView.addItemDecoration(decor)
        adapter!!.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                decor.invalidateHeaders()
            }
        })
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
        adapter!!.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        // nic
    }

    companion object {
        val TAG = "EventsFragment"
    }
}
