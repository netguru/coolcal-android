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
        val startDt = LocalDateTime(now).toLocalDate().toDateTimeAtStartOfDay().millis
        val maxDt = startDt + TimeUnit.DAYS.toMillis(1)
        val events = listOf(Event(1, "1", startDt + hour*8, startDt + hour*12),
                            Event(2, "2", startDt + hour*10, startDt + hour*14),
                            Event(3, "3", startDt - hour*2, startDt + hour*20))

        Log.i("Timeline", "min = $startDt, max = $maxDt")
        eventTimeline.startDateTime = startDt
        eventTimeline.timeSpan = hour * 24
        eventTimeline.events = events
    }
}