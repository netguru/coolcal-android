package co.netguru.android.coolcal.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import butterknife.bindView
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.model.Event
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.concurrent.TimeUnit

class TestActivity : AppCompatActivity() {

    val eventTimeline: EventTimelineView by bindView(R.id.event_timeline_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val now = System.currentTimeMillis()
        val hour = TimeUnit.HOURS.toMillis(1)
        val minDt = LocalDateTime(now).toLocalDate().toDateTimeAtStartOfDay().millis
        val maxDt = minDt + TimeUnit.DAYS.toMillis(1)
        val events = listOf(Event(1, "1", minDt + hour*8, minDt + hour*12),
                            Event(2, "2", minDt + hour*10, minDt + hour*14),
                            Event(3, "3", minDt + hour*2, minDt + hour*20))

        Log.i("Timeline", "min = $minDt, max = $maxDt")
        eventTimeline.minDt = minDt
        eventTimeline.maxDt = maxDt
        eventTimeline.events = events
        eventTimeline.invalidate()
    }
}