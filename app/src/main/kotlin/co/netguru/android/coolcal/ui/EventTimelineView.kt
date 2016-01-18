package co.netguru.android.coolcal.ui

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.os.Build
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
 * example: day timeline with scale every 30min and text ind. every hour
 */

class EventTimelineView : View {

    /*
        Units enum
     */
    companion object {
        private const val TAG = "EventTimelineView"

        private const val TIMESTAMP_PATTERN = "00:00:0000"

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
        paint.color = Color.BLACK
        paint.isElegantTextHeight = true
        paint.isAntiAlias = true
        paint.textSize = 20f
        paint.textAlign = Paint.Align.CENTER
        paint
    }

    private val titleTextPaint: TextPaint by lazy {
        val paint = TextPaint()
        paint.color = Color.BLACK
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
    private var _barHeight: Int = 0
    public var barHeight: Int   // px
        get() = _barHeight
        set(value) {
            _barHeight = value
            invalidate()
        }
    private var _barSpacing: Int = 0
    public var barSpacing: Int   // px
        get() = _barSpacing
        set(value) {
            _barSpacing = value
            invalidate()
        }
    private var _borderSpacing: Int = 0
    public var borderSpacing: Int   // px
        get() = _borderSpacing
        set(value) {
            _borderSpacing = value
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
    private var unitWidth: Int = 0
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
    private var _showTime: Boolean = false
    private var showTime: Boolean
        get() = _showTime
        set(value) {
            _showTime = value
            invalidate()
        }
    private var _showTitles: Boolean = false
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
    private val textRect: Rect by lazy { Rect() }
    private var timeTextHeight: Int = 0
    private var titlesSumHeight: Int = 0

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
                    _barHeight = a.getDimensionPixelSize(attr, barHeight)

                R.styleable.EventTimelineView_barSpacing ->
                    _barSpacing = a.getDimensionPixelSize(attr, barSpacing)

                R.styleable.EventTimelineView_borderSpacing ->
                    _borderSpacing = a.getDimensionPixelSize(attr, borderSpacing)

                R.styleable.EventTimelineView_unitWidth ->
                    unitWidth = a.getDimensionPixelSize(attr, unitWidth)

                R.styleable.EventTimelineView_markTime ->
                    _markTime = a.getFloat(attr, markTime)

                R.styleable.EventTimelineView_markScale ->
                    _markScale = a.getFloat(attr, markScale)

                R.styleable.EventTimelineView_timelineUnit ->
                    _timelineUnit = a.getInt(attr, timelineUnit)

                R.styleable.EventTimelineView_scaleColor ->
                    scalePaint.color = a.getColor(attr, scalePaint.color)

                R.styleable.EventTimelineView_defaultBarColor ->
                    barPaint.color = a.getColor(attr, barPaint.color)

                R.styleable.EventTimelineView_showTime ->
                    _showTime = a.getBoolean(attr, showTime)

                R.styleable.EventTimelineView_showTitles ->
                    _showTitles = a.getBoolean(attr, showTitles)

                R.styleable.EventTimelineView_showScale ->
                    _showScale = a.getBoolean(attr, showScale)

                R.styleable.EventTimelineView_timeTextSize ->
                    timeTextPaint.textSize = a.getDimension(attr, timeTextPaint.textSize)

                R.styleable.EventTimelineView_titleTextSize ->
                    titleTextPaint.textSize = a.getDimension(attr, titleTextPaint.textSize)

                R.styleable.EventTimelineView_startDateTime -> Unit // todo
                R.styleable.EventTimelineView_timeSpan -> Unit // todo
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
        drawScale(canvas)
        drawBars(canvas)
        drawTimeText(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //Get size requested and size mode
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
        // if wrap-content, uses attribute-specified unit width * units to determine
            MeasureSpec.AT_MOST -> {
                widthSize
            } // todo: !!! nie ma jeszcze osobnej obslugi wrapa
            else -> widthSize
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> measureHeight()
            else -> heightSize
        }

        setMeasuredDimension(width, height)
    }

    private fun measureHeight(): Int {
        val borders = borderSpacing * 2
        val bars = barHeight * events.size
        val spacings = barSpacing * (events.size - 1)
        // calculating text heights
        timeTextHeight = if (showTime) {
            timeTextPaint.getTextBounds(TIMESTAMP_PATTERN, 0, TIMESTAMP_PATTERN.length, textRect)
            textRect.height()
        } else 0

        titlesSumHeight = if (showTitles) {
            events.map { event ->
                titleTextPaint.getTextBounds(event.title, 0, event.title.length, textRect)
                textRect.height()
            }.sum()
        } else 0

        return borders + bars + spacings + timeTextHeight + titlesSumHeight
    }

    private fun drawBars(canvas: Canvas) {
        var textHeightAccumulator = 0
        val shiftY = if(showTime) timeTextHeight else 0
        events.forEachIndexed { i, event ->
            val start = if (event.dtStart < startDt) startDt else event.dtStart
            val stop = if (event.dtStop > stopDt) stopDt else event.dtStop

            val startX = normForRange(start) * w
            val stopX = normForRange(stop) * w
            val startY = (shiftY + borderSpacing + textHeightAccumulator +
                    i * (barHeight + barSpacing)).toFloat()

            val stopY = startY + barHeight

            if (showTitles) {
                titleTextPaint.getTextBounds(event.title, 0, event.title.length, textRect)
                val currentTitleHeight = textRect.height()
                textHeightAccumulator += currentTitleHeight
                canvas.drawText(event.title, startX, stopY + currentTitleHeight, titleTextPaint)
            }

            // Log.d("Timeline", "drawing: ($startX,$Y)->($stopX,$Y)")
            barRectF.set(startX, startY, stopX, stopY)
            canvas.drawRoundRect(barRectF, barRadius, barRadius, barPaint)

        }
    }

    private fun drawScale(canvas: Canvas) {
        Log.d("time span", "$startDt <-> $stopDt")
        Log.d("scaleDrawRange", scaleDrawRange!!.toString())

        val startY = timeTextHeight.toFloat()

        if (showScale) {
            for (i in scaleDrawRange!!) {
                val x = normForRange(i) * w
                canvas.drawLine(x, startY, x, h.toFloat(), scalePaint)
            }
        }
    }

    private fun drawTimeText(canvas: Canvas) {
        if (showTime) {
            for (i in timeMarkDrawRange!!) {
                val x = normForRange(i) * w
                val timeText = formatTime(timeMillis = i)
                canvas.drawText(timeText, x, 0f + timeTextHeight, timeTextPaint)
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
        Log.i(TAG, "invalidate()")
    }

    private fun formatTime(timeMillis: Long) = DateTime(timeMillis).toLocalTime().toString("hh:mm")
}
