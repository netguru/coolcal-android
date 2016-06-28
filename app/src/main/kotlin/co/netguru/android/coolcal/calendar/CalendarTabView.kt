package co.netguru.android.coolcal.calendar

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.bindViews
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.rendering.TimeFormatter
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringSystem
import javax.inject.Inject

class CalendarTabView : LinearLayout {

    @Inject lateinit var timeFormatter: TimeFormatter

    private var currentPos = 0

    private val frames: List<View> by bindViews(R.id.day0, R.id.day1,
            R.id.day2, R.id.day3, R.id.day4)
    private val domTextViews: List<TextView> by bindViews(R.id.day0_dom_text_view,
            R.id.day1_dom_text_view, R.id.day2_dom_text_view,
            R.id.day3_dom_text_view, R.id.day4_dom_text_view)
    private val dowTextViews: List<TextView> by bindViews(R.id.day0_dow_text_view,
            R.id.day1_dow_text_view, R.id.day2_dow_text_view,
            R.id.day3_dow_text_view, R.id.day4_dow_text_view)

    private var _days: List<Long>? = null
    var days: List<Long>?
        get() = _days
        set(value) {
            _days = value
            updateDayTextViews()
        }

    private val springSys = SpringSystem.create()
    private val springs = (0..5).map { springSys.createSpring() }
    private val springListener = object : SimpleSpringListener() {
        override fun onSpringUpdate(spring: Spring?) {
            val value = spring!!.currentValue
            val sizeScale = (1 + (value * 0.5)).toFloat();
            val view = when (spring) {
                springs[0] -> domTextViews[0]
                springs[1] -> domTextViews[1]
                springs[2] -> domTextViews[2]
                springs[3] -> domTextViews[3]
                springs[4] -> domTextViews[4]
                else -> null
            }
            view?.scaleX = sizeScale
            view?.scaleY = sizeScale
        }
    }

    private var _dayClickListener: OnDayClickListener? = null
    var dayClickListener: OnDayClickListener?
        get() = _dayClickListener
        set(value) {
            _dayClickListener = value
        }

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        //Set click listeners for days views.
        fun setFramesListeners() {
            for (i in 0..frames.size - 1) {
                frames[i].setOnClickListener({
                    if (dayClickListener != null) {
                        (dayClickListener as OnDayClickListener).onDayClick(days!![i])
                    }
                })
            }
        }
        App.component.inject(this)
        inflate(context, R.layout.view_calendar_tabs, this)
        this.orientation = LinearLayout.HORIZONTAL
        setFramesListeners()
        addOnLayoutChangeListener({ v, left, top, right, bottom, oldLeft,
                                    oldTop, oldRight, oldBottom ->
            resetPivots()
        })
    }

    fun switchDay(startDayDt: Long) {
        try {
            val nextActive = days!!.indexOf(startDayDt)
            switchActive(nextActive)
        } catch (e: ArrayIndexOutOfBoundsException) {

        }
    }

    fun areClickableDays(clickable: Boolean) {
        for (view in frames) {
            view.isClickable = clickable
        }
    }

    private fun switchActive(newPos: Int) {
        springs[currentPos].endValue = 0.0
        springs[newPos].endValue = 1.0
        this.currentPos = newPos
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        switchActive(currentPos)
        areClickableDays(false)
        springs.forEach { spring -> spring.addListener(springListener) }
    }

    override fun onDetachedFromWindow() {
        springs.forEach { spring -> spring.removeListener(springListener) }
        dayClickListener = null
        super.onDetachedFromWindow()
    }

    private fun resetPivots() {
        domTextViews.forEachIndexed { i, view ->
            view.pivotY = 0f
            view.pivotX = frames[i].width / 2f
        }
    }

    private fun updateDayTextViews() {
        domTextViews.forEachIndexed { i, view ->
            view.text = timeFormatter.formatDayOfMonth(days!![i])
        }
        dowTextViews.forEachIndexed { i, view ->
            view.text = timeFormatter.formatDayOfWeekShort(days!![i])
        }
    }

    /**
     * Listener to handle click on Day from calendar tab.
     */
    interface OnDayClickListener {

        /**
         * Callback for day click.
         * @param dayInMillis Clicked day in milliseconds.
         */
        fun onDayClick(dayInMillis: Long)
    }

}