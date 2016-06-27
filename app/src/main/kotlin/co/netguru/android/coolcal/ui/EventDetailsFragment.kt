package co.netguru.android.coolcal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.calendar.Event
import co.netguru.android.coolcal.rendering.TimeFormatter
import kotlinx.android.synthetic.main.fragment_event_details.*
import javax.inject.Inject

class EventDetailsFragment : BaseFragment() {

    companion object {
        val TAG = EventDetailsFragment::class.java.simpleName
        val FRAGMENT_EVENT_DETAILS_BUNDLE = "fragment_event_details_bundle"

        @JvmStatic fun newInstance(event: Event): EventDetailsFragment {
            val fragment = EventDetailsFragment()
            val args = Bundle()
            args.putParcelable(FRAGMENT_EVENT_DETAILS_BUNDLE, event)
            fragment.arguments = args

            return fragment
        }
    }

    init {
        App.component.inject(this)
    }

    @Inject lateinit var timeFormatter: TimeFormatter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val containsEvent = arguments.containsKey(FRAGMENT_EVENT_DETAILS_BUNDLE)
        if (!containsEvent) {
            throw IllegalArgumentException("Event details should contain event object")
        }
        initViews(arguments.getParcelable<Event>(FRAGMENT_EVENT_DETAILS_BUNDLE))
    }

    private fun initViews(event: Event) {
        eventIconClose.setOnClickListener { activity.finish() }
        eventTitleLayout.setPadding(0, getStatusBarHeight(), 0, 0)
        if (event.displayColor != null) {
            eventTitleLayout.setBackgroundColor(event.displayColor)
        }
        eventDetailsTitleText.text = event.title ?: ""

        eventDetailsTimeText.text = timeFormatter.formatLongDate(event.begin, event.end, event.isAllDay)

        if (event.location.isNullOrEmpty()) {
            eventDetailsLocationLayout.visibility = View.GONE
        } else {
            eventDetailsLocationText.text = event.location
        }

        if (event.description.isNullOrEmpty()) {
            eventDetailsNotesLayout.visibility = View.GONE
        } else {
            eventDetailsNotesText.text = event.description
        }

        if (event.owner.isNullOrEmpty()) {
            eventDetailsOwnerLayout.visibility = View.GONE
        } else {
            eventDetailsOwnerText.text = event.owner
        }
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
