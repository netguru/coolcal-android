package co.netguru.android.coolcal.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.calendar.Event

class EventDetailsActivity : AppCompatActivity() {

    companion object {
        val EVENT_DETAILS_BUNDLE = "event_details_bundle"

        @JvmStatic fun newIntent(context: Context, event: Event) {
            val intent = Intent(context, EventDetailsActivity::class.java)
            intent.putExtra(EVENT_DETAILS_BUNDLE, event)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
    }
}
