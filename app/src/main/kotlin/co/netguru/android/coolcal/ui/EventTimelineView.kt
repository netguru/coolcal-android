package co.netguru.android.coolcal.ui

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.SystemClock
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.model.Event
import org.joda.time.DateTime
import java.lang.Math.ceil
import java.lang.Math.floor
import java.util.concurrent.TimeUnit

/**
 * example: day timeline with scale every hour and time mark every 2 hours
 *
 * todo: border cases: event start/stop dt out of time span
 * todo: solve edge trimming of titles/time marks etc.,
 * todo: avoid time mark overlapping: adjusting unitWidth (unspecified width)
 * todo: avoid time mark overlapping: adjusting timeTextPaint.textSize (exactly/at_most width)
 * todo: showing current time
 * todo: check for more possible drawing opt.
 */

open class EventTimelineView : View {

    /*
        Units enum
     */
    companion object {
        private const val TAG = "EventTimelineView"

        public const val MILLISECOND = 0
        public const val SECOND = 1
        public const val MINUTE = 2
        public const val HOUR = 3
        public const val DAY = 4

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

    /*
        Event bar
     */
    private var _barRadius: Float = 20f
    private var barRadius: Float
        get() = _barRadius
        set(value) {
            _barRadius = value
            invalidate()
        }
    private var _barHeight: Float = 12f
    public var barHeight: Float   // px
        get() = _barHeight
        set(value) {
            _barHeight = value
            invalidate()
        }
    private var _barSpacing: Float = 0f
    public var barSpacing: Float   // px
        get() = _barSpacing
        set(value) {
            _barSpacing = value
            invalidate()
        }
    private var _topSpacing: Float = 0f
    public var topSpacing: Float
        get() = _topSpacing
        set(value) {
            _topSpacing = value
            invalidate()
        }
    private var _bottomSpacing: Float = 0f
    public var bottomSpacing: Float
        get() = _bottomSpacing
        set(value) {
            _bottomSpacing = value
            invalidate()
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
            invalidate()
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
            invalidate()
        }
    private var _markScale: Float = 1f
    private var markScale: Float
        get() = _markScale
        set(value) {
            _markScale = value
            invalidate()
        }

    /*
        Show flags
     */
    private var _showTime: Boolean = true
    private var showTime: Boolean
        get() = _showTime
        set(value) {
            _showTime = value
            invalidate()
        }
    private var _showTitles: Boolean = true
    private var showTitles: Boolean
        get() = _showTitles
        set(value) {
            _showTitles = value
            invalidate()
        }
    private var _showScale: Boolean = true
    private var showScale: Boolean
        get() = _showScale
        set(value) {
            _showScale = value
            invalidate()
        }

    /*
        Span
     */
    private var _timeSpan: Long = 0L
    public var timeSpan: Long
        get() = _timeSpan
        set(value) {
            _timeSpan = value
            stopDt = startDt + timeSpan
            recalculateDrawRanges()
            invalidate()
        }
    private var _startDt: Long = 0L
    public var startDt: Long
        get() = _startDt
        set(value) {
            _startDt = value
            stopDt = startDt + timeSpan
            recalculateDrawRanges()
            invalidate()
        }
    private var stopDt: Long = 0L

    /*
        Data set
     */
    private var _events = emptyList<Event>()
    public var events: List<Event>
        get() = _events
        set(value) {
            _events = value
            invalidate()
        }

    private val barRectF = RectF()

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

                R.styleable.EventTimelineView_topSpacing ->
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

                R.styleable.EventTimelineView_scaleColor ->
                    scalePaint.color = a.getColor(attr, scalePaint.color)

                R.styleable.EventTimelineView_defaultBarColor ->
                    barPaint.color = a.getColor(attr, barPaint.color)

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
                    throw UnsupportedOperationException() // todo
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

    private fun normForRange(value: Long) = (value - startDt).toFloat() / timeSpan

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas!!)
        val start = SystemClock.elapsedRealtime()
        drawScale(canvas)
        drawBars(canvas)
        drawTimeText(canvas)
        val delta = (SystemClock.elapsedRealtime() - start) / 1000f
        Log.i(TAG, "Draw executed in ${delta}s")
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
        Log.d(TAG, "Measured dimension: $width x $height")
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(): Int =
            ((timeSpan.toFloat() / unitMillis(timelineUnit)) * unitWidth).toInt()

    private fun measureHeight(): Int {
        val borders = topSpacing + bottomSpacing
        val bars = barHeight * events.size
        val spacings = barSpacing * (events.size - 1)
        val timeTextHeight = timeTextHeight()
        val titlesSumHeight = titleTextHeight() * events.size
        return (borders + bars + spacings + timeTextHeight + titlesSumHeight).toInt()
    }

    private fun titleTextHeight() = when(showTitles) {
        true -> titleTextPaint.fontMetrics.descent - titleTextPaint.fontMetrics.top
        false -> 0f
    }
    private fun timeTextHeight() = when (showTime) {
        true -> timeTextPaint.fontMetrics.bottom - timeTextPaint.fontMetrics.top
        false -> 0f
    }

    private fun drawBars(canvas: Canvas) {
        events.forEachIndexed { i, event ->
            val start = if (event.dtStart < startDt) startDt else event.dtStart
            val stop = if (event.dtStop > stopDt) stopDt else event.dtStop
            val startX = normForRange(start) * w
            val stopX = normForRange(stop) * w
            val startY = (timeTextHeight() + topSpacing +
                    i * (barHeight + titleTextHeight() + barSpacing)).toFloat()
            val stopY = startY + barHeight

            barRectF.set(startX, startY, stopX, stopY)
            canvas.drawRoundRect(barRectF, barRadius, barRadius, barPaint)

            if (showTitles) {
                canvas.drawText(event.title,
                        startX, stopY - titleTextPaint.fontMetrics.ascent, titleTextPaint)
            }
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

    private fun recalculateDrawRanges() {
        val unitMillis = unitMillis(timelineUnit)
        val baseRange = ceil(startDt.toDouble() / unitMillis).toLong() * unitMillis..
                floor(stopDt.toDouble() / unitMillis).toLong() * unitMillis
        scaleDrawRange = baseRange step (unitMillis * markScale).toLong()
        timeMarkDrawRange = baseRange step (unitMillis * markTime).toLong()
    }

    override fun invalidate() {
        super.invalidate()
        Log.d(TAG, "invalidate()")
    }

    open internal fun formatTime(timeMillis: Long) =
            DateTime(timeMillis).toLocalTime().toString("hh:mm")
}
