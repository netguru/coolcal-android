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
import co.netguru.android.coolcal.utils.AppPreferences
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringListener
import com.facebook.rebound.SpringSystem

class CalendarTabView : LinearLayout, SpringListener {

    private var currentPos = 0
    private val frames: List<View> by bindViews(R.id.day0, R.id.day1,
            R.id.day2, R.id.day3, R.id.day4)
    private val textViews: List<TextView> by bindViews(R.id.day0_dom_text_view,
            R.id.day1_dom_text_view, R.id.day2_dom_text_view,
            R.id.day3_dom_text_view, R.id.day4_dom_text_view)

    private val springSys = SpringSystem.create()
    private val springs = (0..5).map { springSys.createSpring() }

    private var _days: List<Long>? = null
    var days: List<Long>?
        get() = _days
        set(value) {
            _days = value
            updateDayTextViews()
        }

    private fun updateDayTextViews() {
        textViews.forEachIndexed { i, view ->
            view.text = AppPreferences.formatDayOfMonth(days!![i])
        }
    }

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initFromAttributes(context, attrs, defStyleAttr, 0)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initFromAttributes(context: Context, attrs: AttributeSet?,
                                   defStyleAttr: Int, defStyleRes: Int) {

        inflate(context, R.layout.view_calendar_tabs, this)
        this.orientation = LinearLayout.HORIZONTAL
    }

    public fun switchDay(startDayDt: Long) {
        val nextActive = days!!.indexOf(startDayDt)
        switchActive(nextActive)
    }

    private fun switchActive(newPos: Int) {
        springs[currentPos].setEndValue(0.0)
        springs[newPos].setEndValue(1.0)
        this.currentPos = newPos
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        switchActive(currentPos)
        springs.forEach { spring -> spring.addListener(this) }
    }

    override fun onDetachedFromWindow() {
        springs.forEach { spring -> spring.removeListener(this) }
        super.onDetachedFromWindow()
    }

    override fun onSpringEndStateChange(spring: Spring?) {
        //        throw UnsupportedOperationException()
    }

    override fun onSpringActivate(spring: Spring?) {
        //        throw UnsupportedOperationException()
    }

    override fun onSpringUpdate(spring: Spring?) {
        val value = spring!!.currentValue
        val sizeScale = (1 + (value * 1)).toFloat();
        val view = when (spring) {
            springs[0] -> frames[0]
            springs[1] -> frames[1]
            springs[2] -> frames[2]
            springs[3] -> frames[3]
            springs[4] -> frames[4]
            else -> null
        }
        view?.scaleX = sizeScale
        view?.scaleY = sizeScale
    }

    override fun onSpringAtRest(spring: Spring?) {
        //        throw UnsupportedOperationException()
    }
}