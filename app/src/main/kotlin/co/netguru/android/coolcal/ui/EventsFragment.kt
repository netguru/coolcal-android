package co.netguru.android.coolcal.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.model.Event
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import java.util.concurrent.TimeUnit

class EventsFragment : Fragment(), EventsLoader.EventsListListener {

    val recyclerView: RecyclerView by bindView(R.id.events_recyclerview)
    val adapter = EventsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val now = System.currentTimeMillis()
        val weekLater = now + TimeUnit.DAYS.toMillis(7)
        val data = Bundle()
        data.putLong(EventsLoader.ARG_DT_FROM, now)
        data.putLong(EventsLoader.ARG_DT_TO, weekLater)

        val loader = EventsLoader(context, this)
        activity.supportLoaderManager.initLoader(EventsLoader.ID, data, loader)
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
        adapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                decor.invalidateHeaders()
            }
        })
    }

    override fun onReset() {
        adapter.events = emptyList()
    }

    override fun onLoad(events: List<Event>) {
        adapter.events = events
    }
}
