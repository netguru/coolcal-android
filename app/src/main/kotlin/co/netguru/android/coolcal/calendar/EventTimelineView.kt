package co.netguru.android.coolcal.calendar

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.app.App
import co.netguru.android.coolcal.rendering.TimeFormatter
import org.joda.time.DateTime
import java.lang.Math.ceil
import java.lang.Math.floor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * example: day timeline with scale every hour and time mark every 2 hours
 *
 * todo: border cases: event start/stop dt out of time span
 * todo: solve edge trimming of titles/time marks etc.,
 * todo: avoid time mark overlapping: adjusting unitWidth (unspecified width)
 * todo: avoid time mark overlapping: adjusting timeTextPaint.textSize (exactly/at_most width)
 * todo: showing current time
 * todo: check for more possible drawing opt.
 * todo: "no events" message while empty or sth like that
 */

class EventTimelineView : View {

    /**
     * Adapter interface. The EventTimelineView will use an instance of this adapter to obtain
     * all the data necessary to render the timeline.
     */
    interface Adapter {
        fun isItemAllDay(position: Int): Boolean
        fun getItemDateStart(position: Int): Long
        fun getItemDateStop(position: Int): Long
        fun getItemColor(position: Int): Int?
        fun getItemTitle(position: Int): String
        fun getItemCount(): Int
    }

    companion object {
        const val MILLISECOND = 0
        const val SECOND = 1
        const val MINUTE = 2
        const val HOUR = 3
        const val DAY = 4
        private fun unitMillis(unit: Int): Long =
                when (unit) {
                    MILLISECOND -> 1L
                    SECOND -> TimeUnit.SECONDS.toMillis(1)
                    MINUTE -> TimeUnit.MINUTES.toMillis(1)
                    HOUR -> TimeUnit.HOURS.toMillis(1)
                    DAY -> TimeUnit.DAYS.toMillis(1)
                    else -> {
                        throw IllegalArgumentException("Invalid unit")
                    }
                }
    }

    @Inject lateinit var timeFormatter: TimeFormatter

    /*
        Adapter
     */
    private var _adapter: Adapter? = null
    var adapter: Adapter?
        get() = _adapter
        set(value) {
            _adapter = value
        }

    /*
        Paints
     */
    private val barPaint: Paint by lazy {
        val paint = Paint()
        paint.color = Color.RED
        paint.isAntiAlias = true
        paint
    }

    private val scalePaint: Paint by lazy {
        val paint = Paint()
        paint.color = Color.LTGRAY
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.BUTT
        paint.strokeWidth = 0f
        paint
    }

    private val timeIndicatorLinePaint: Paint by lazy {
        val paint = Paint()
        paint.color = timeIndicatorColor
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.BUTT
        paint.strokeWidth = timeIndicatorSize
        paint
    }

    private val timeIndicatorPaint: Paint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.BUTT
        paint
    }

    private val timeTextPaint: TextPaint by lazy {
        val paint = TextPaint()
        paint.color = Color.GRAY
        paint.isElegantTextHeight = true
        paint.isAntiAlias = true
        paint.textSize = 20f
        paint.textAlign = Paint.Align.CENTER
        paint
    }

    private val titleTextPaint: TextPaint by lazy {
        val paint = TextPaint()
        paint.color = Color.GRAY
        paint.isElegantTextHeight = true
        paint.isAntiAlias = true
        paint.textSize = 20f
        paint.textAlign = Paint.Align.LEFT
        paint
    }

    /*
        Draw sets
     */
    private var scaleDrawRange: LongProgression? = null
    private var timeMarkDrawRange: LongProgression? = null

    /*
        Size
     */
    private var w = 0
    private var h = 0
    private val timeIndicatorVerticalSpacing = 2f
    private val timeIndicatorDrawableTopMargin = 8

    /*
        Event bar
     */
    private var _barRadius: Float = 20f
    private var barRadius: Float
        get() = _barRadius
        set(value) {
            _barRadius = value
        }
    private var _barHeight: Float = 12f
    var barHeight: Float   // px
        get() = _barHeight
        set(value) {
            _barHeight = value
        }
    private var _barSpacing: Float = 0f
    var barSpacing: Float   // px
        get() = _barSpacing
        set(value) {
            _barSpacing = value
        }
    private var _topSpacing: Float = 0f
    var topSpacing: Float
        get() = _topSpacing
        set(value) {
            _topSpacing = value
        }
    private var _bottomSpacing: Float = 0f
    var bottomSpacing: Float
        get() = _bottomSpacing
        set(value) {
            _bottomSpacing = value
        }

    /*
        Time span
     */
    private var _timelineUnit: Int = HOUR
    private var timelineUnit: Int
        get() = _timelineUnit
        set(value) {
            _timelineUnit = value
            recalculateDrawRanges()
        }
    private var _unitWidth: Float = 80f
    private var unitWidth: Float
        get() = _unitWidth
        set(value) {
            _unitWidth = value
        }
    private var _markTime: Float = 1f
    private var markTime: Float
        get() = _markTime
        set(value) {
            _markTime = value
        }
    private var _markScale: Float = 1f
    private var markScale: Float
        get() = _markScale
        set(value) {
            _markScale = value
        }

    /*
        Show flags
     */
    private var _showTime: Boolean = true
    private var showTime: Boolean
        get() = _showTime
        set(value) {
            _showTime = value
        }
    private var _showTitles: Boolean = true
    private var showTitles: Boolean
        get() = _showTitles
        set(value) {
            _showTitles = value
        }
    private var _showScale: Boolean = true
    private var showScale: Boolean
        get() = _showScale
        set(value) {
            _showScale = value
        }

    /*
        Time indicator
     */
    private var _showTimeIndicator: Boolean = true
    private var showTimeIndicator: Boolean
        get() = _showTimeIndicator
        set(value) {
            _showTimeIndicator = value
        }
    private var _timeIndicatorSize: Float = 14f
    private var timeIndicatorSize: Float
        get() = _timeIndicatorSize
        set(value) {
            _timeIndicatorSize = value
        }
    private var _timeIndicatorColor: Int = Color.GRAY
    private var timeIndicatorColor: Int
        get() = _timeIndicatorColor
        set(value) {
            _timeIndicatorColor = value
        }
    private var _timeIndicatorDrawable: Int = R.drawable.current_time_indicator_calendar
    private var timeIndicatorDrawable: Int
        get() = _timeIndicatorDrawable
        set(value) {
            _timeIndicatorDrawable = value
        }
    /*
        Span
     */
    private var _timeSpan: Long = 0L
    var timeSpan: Long
        get() = _timeSpan
        set(value) {
            _timeSpan = value
            timelineDtStop = timelineDtStart + timeSpan
            recalculateDrawRanges()
        }
    private var _timelineDtStart: Long = 0L
    var timelineDtStart: Long
        get() = _timelineDtStart
        set(value) {
            _timelineDtStart = value
            timelineDtStop = timelineDtStart + timeSpan
            recalculateDrawRanges()
        }
    private var timelineDtStop: Long = 0L

    /*
        Color
     */
    private val barRectF = RectF()
    private var defaultColor = Color.BLACK

    inline fun refresh(crossinline block: EventTimelineView.() -> Unit) {
        block()
        while (!isInLayout) {
            requestLayout()
            break
        }
        invalidate()
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

        App.component.inject(this)

        val a = context.obtainStyledAttributes(attrs, R.styleable.EventTimelineView,
                defStyleAttr, defStyleRes)
        for (i in 0..a.indexCount) {
            val attr = a.getIndex(i)
            when (attr) { // todo: defaults, exceptions
                R.styleable.EventTimelineView_barRadius ->
                    _barRadius = a.getDimension(attr, barRadius)

                R.styleable.EventTimelineView_barHeight ->
                    _barHeight = a.getDimension(attr, barHeight)

                R.styleable.EventTimelineView_barSpacing ->
                    _barSpacing = a.getDimension(attr, barSpacing)

                R.styleable.EventTimelineView_topSpacing ->
                    _topSpacing = a.getDimension(attr, topSpacing)

                R.styleable.EventTimelineView_bottomSpacing ->
                    _bottomSpacing = a.getDimension(attr, bottomSpacing)

                R.styleable.EventTimelineView_unitWidth ->
                    _unitWidth = a.getDimension(attr, unitWidth)

                R.styleable.EventTimelineView_markTime ->
                    _markTime = a.getFloat(attr, markTime)

                R.styleable.EventTimelineView_markScale ->
                    _markScale = a.getFloat(attr, markScale)

                R.styleable.EventTimelineView_timelineUnit ->
                    _timelineUnit = a.getInt(attr, timelineUnit)

                R.styleable.EventTimelineView_showTime ->
                    _showTime = a.getBoolean(attr, showTime)

                R.styleable.EventTimelineView_showTitles ->
                    _showTitles = a.getBoolean(attr, showTitles)

                R.styleable.EventTimelineView_showScale ->
                    _showScale = a.getBoolean(attr, showScale)

                R.styleable.EventTimelineView_showTimeIndicator ->
                    _showTimeIndicator = a.getBoolean(attr, showTimeIndicator)

                R.styleable.EventTimelineView_timeIndicatorSize ->
                    _timeIndicatorSize = a.getDimension(attr, timeIndicatorSize)

                R.styleable.EventTimelineView_timeIndicatorColor ->
                    _timeIndicatorColor = a.getColor(attr, timeIndicatorColor)

                R.styleable.EventTimelineView_timeIndicatorDrawable ->
                    _timeIndicatorDrawable = a.getColor(attr, timeIndicatorDrawable)

                R.styleable.EventTimelineView_scaleColor ->
                    scalePaint.color = a.getColor(attr, scalePaint.color)

                R.styleable.EventTimelineView_defaultBarColor ->
                    defaultColor = a.getColor(attr, defaultColor)

                R.styleable.EventTimelineView_eventTitleTextColor ->
                    titleTextPaint.color = a.getColor(attr, titleTextPaint.color)

                R.styleable.EventTimelineView_timeTextColor ->
                    timeTextPaint.color = a.getColor(attr, timeTextPaint.color)

                R.styleable.EventTimelineView_timeTextSize ->
                    timeTextPaint.textSize = a.getDimension(attr, timeTextPaint.textSize)

                R.styleable.EventTimelineView_titleTextSize ->
                    titleTextPaint.textSize = a.getDimension(attr, titleTextPaint.textSize)

                R.styleable.EventTimelineView_startDateTime ->
                    throw UnsupportedOperationException() // todo

                R.styleable.EventTimelineView_timeSpan ->
                    throw UnsupportedOperationException() // todo (use timeline unit)
            }
        }
        a.recycle()

        recalculateDrawRanges()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
    }

    private fun normForRange(value: Long) = (value - timelineDtStart).toFloat() / timeSpan

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas!!)
        drawScale(canvas)
        drawTimeIndicator(canvas)
        drawBars(canvas)
        drawTimeText(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> widthSize
            else -> measureWidth()
        }
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> heightSize
            else -> measureHeight()
        }
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(): Int =
            ((timeSpan.toFloat() / unitMillis(timelineUnit)) * unitWidth).toInt()

    private fun measureHeight(): Int {
        val size = adapter?.getItemCount() ?: 0
        val borders = topSpacing() + bottomSpacing
        val bars = barHeight * size
        val spacings = barSpacing * (size - 1)
        val timeTextHeight = timeTextHeight()
        val titlesSumHeight = titleTextHeight() * size
        return (borders + bars + spacings + timeTextHeight + titlesSumHeight).toInt()
    }

    private fun titleTextHeight() = when (showTitles) {
        true -> titleTextPaint.fontMetrics.descent - titleTextPaint.fontMetrics.top
        false -> 0f
    }

    private fun timeTextHeight() = when (showTime) {
        true -> timeTextPaint.fontMetrics.bottom - timeTextPaint.fontMetrics.top
        false -> 0f
    }

    private fun topSpacing() = when (showTimeIndicator && isCurrentDay()) {
        true -> topSpacing + indicatorHeight()
        false -> topSpacing
    }

    private fun prepareRect(rectF: RectF, i: Int, dtStart: Long, dtStop: Long) {
        val start = if (dtStart < timelineDtStart) timelineDtStart else dtStart
        val stop = if (dtStop > timelineDtStop) timelineDtStop else dtStop
        val startX = normForRange(start) * w
        val stopX = normForRange(stop) * w
        val startY = (timeTextHeight() + topSpacing() +
                i * (barHeight + titleTextHeight() + barSpacing)).toFloat()
        val stopY = startY + barHeight

        rectF.set(startX, startY, stopX, stopY)
    }

    private fun drawBars(canvas: Canvas) {
        val events = 0 until (adapter?.getItemCount() ?: 0)

        events.forEach { i ->
            val allDay = adapter!!.isItemAllDay(i)
            val dtStart = if (allDay) timelineDtStart else adapter!!.getItemDateStart(i)
            val dtStop = if (allDay) timelineDtStop else adapter!!.getItemDateStop(i)
            prepareRect(barRectF, i, dtStart, dtStop)

            val displayColor = adapter!!.getItemColor(i)
            barPaint.color = displayColor ?: defaultColor

            if (showTitles) {
                val title = adapter!!.getItemTitle(i)
                canvas.drawText(title,
                        barRectF.left,
                        barRectF.bottom - titleTextPaint.fontMetrics.top,
                        titleTextPaint)
            }
            canvas.drawRoundRect(barRectF, barRadius, barRadius, barPaint)
        }
    }

    private fun drawScale(canvas: Canvas) {
        if (showScale) {
            for (i in scaleDrawRange!!) {
                val x = normForRange(i) * w
                canvas.drawLine(x, timeTextHeight(), x, h.toFloat(), scalePaint)
            }
        }
    }

    private fun drawTimeText(canvas: Canvas) {
        if (showTime) {
            for (i in timeMarkDrawRange!!) {
                val x = normForRange(i) * w
                val timeText = formatTime(timeMillis = i)
                canvas.drawText(timeText, x, 0f - timeTextPaint.fontMetricsInt.ascent, timeTextPaint)
            }
        }
    }

    private fun drawTimeIndicator(canvas: Canvas){
        if (showTimeIndicator) {
            if (isCurrentDay()) {
                var localTime = getCurrentTime()
                var localMillis = localTime.millisOfDay.toFloat()
                var time = localMillis / unitMillis(DAY)
                var position = time * measureWidth()
                var top = h.toFloat()

                canvas.drawLine(position, timeTextHeight(), position, top, timeIndicatorLinePaint)

                var bitmap = createIndicatorBitmap()
                var indicatorShift = bitmap.width/2
                canvas.drawBitmap(bitmap, position-indicatorShift, timeTextHeight()
                        + timeIndicatorDrawableTopMargin, timeIndicatorPaint)

            }
        }
    }

    private fun createIndicatorBitmap() = BitmapFactory.decodeResource(resources, timeIndicatorDrawable)

    private fun indicatorHeight() = createIndicatorBitmap().height + 2 * timeIndicatorVerticalSpacing

    private fun isCurrentDay(): Boolean {
        var localTime = getCurrentTime()
        return localTime.dayOfYear == DateTime(timelineDtStart).dayOfYear
    }

    private fun getCurrentTime() = DateTime(System.currentTimeMillis()).toLocalDateTime()


    private fun recalculateDrawRanges() {
        val unitMillis = unitMillis(timelineUnit)
        val baseRange = ceil(timelineDtStart.toDouble() / unitMillis).toLong() * unitMillis..
                floor(timelineDtStop.toDouble() / unitMillis).toLong() * unitMillis
        scaleDrawRange = baseRange step (unitMillis * markScale).toLong()
        timeMarkDrawRange = baseRange step (unitMillis * markTime).toLong()
    }

    private fun formatTime(timeMillis: Long) = timeFormatter.formatTimeOfDay(timeMillis)
}
