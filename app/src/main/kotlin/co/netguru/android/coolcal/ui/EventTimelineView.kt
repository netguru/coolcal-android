package co.netguru.android.coolcal.ui

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.model.Event
import java.util.concurrent.TimeUnit

/**
 * example: day timeline with scale every 30min and text ind. every hour
 */

class EventTimelineView : View {

    /*
        Units enum
     */
    companion object {
        const val MILLISECOND = 0
        const val SECOND = 1
        const val MINUTE = 2
        const val HOUR = 3
        const val DAY = 4

        internal fun unitMillis(unit: Int): Long =
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

    private enum class TimelineUnit(val millis: Long) {

    }

    /*
        Paints
     */
    private val barPaint: Paint by lazy {
        val paint = Paint()
        paint.color = Color.RED
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 4f
        paint
    }

    private val scalePaint: Paint by lazy {
        val paint = Paint()
        paint.color = Color.LTGRAY
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.BUTT
        paint.strokeWidth = 1f
        paint
    }

    /*
        Size
     */
    private var w = 0
    private var h = 0

    /*
        Event bar
     */
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
    private var timelineUnit: Int = HOUR
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
        Styling
     */
    private var fontSize: Float = 0f
    private var scaleColor: Int = Color.LTGRAY
    private var defaultBarColor: Int = Color.RED

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
            invalidate()
        }
    private var _startDateTime: Long = 0L
    public var startDateTime: Long
        get() = _startDateTime
        set(value) {
            _startDateTime = value
            invalidate()
        }

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

    /*
        Def
     */
    private fun stopDateTime() = startDateTime + timeSpan


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
                R.styleable.EventTimelineView_barHeight ->
                    barHeight = a.getDimensionPixelSize(attr, barHeight)

                R.styleable.EventTimelineView_barSpacing ->
                    barSpacing = a.getDimensionPixelSize(attr, barSpacing)

                R.styleable.EventTimelineView_borderSpacing ->
                    borderSpacing = a.getDimensionPixelSize(attr, borderSpacing)

                R.styleable.EventTimelineView_unitWidth ->
                    unitWidth = a.getDimensionPixelSize(attr, unitWidth)

                R.styleable.EventTimelineView_markTime ->
                    markTime = a.getFloat(attr, markTime)

                R.styleable.EventTimelineView_markScale ->
                    markScale = a.getFloat(attr, markScale)

                R.styleable.EventTimelineView_timelineUnit ->
                    timelineUnit = a.getInt(attr, timelineUnit)

                R.styleable.EventTimelineView_scaleColor ->
                    scaleColor = a.getColor(attr, scaleColor)

                R.styleable.EventTimelineView_defaultBarColor ->
                    defaultBarColor = a.getColor(attr, defaultBarColor)

                R.styleable.EventTimelineView_showTime ->
                    showTime = a.getBoolean(attr, showTime)

                R.styleable.EventTimelineView_showTitles ->
                    showTitles = a.getBoolean(attr, showTitles)

                R.styleable.EventTimelineView_showScale ->
                    showScale = a.getBoolean(attr, showScale)

                R.styleable.EventTimelineView_fontSize ->
                    fontSize = a.getFloat(attr, fontSize)

                R.styleable.EventTimelineView_startDateTime ->
                    startDateTime = a.getString(attr).toLong()

                R.styleable.EventTimelineView_timeSpan ->
                    timeSpan = a.getString(attr).toLong()
            }
        }
        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
    }

    private fun normForRange(value: Long) = (value - startDateTime).toFloat() / timeSpan

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        barPaint.strokeWidth = barHeight.toFloat()
        drawBars(canvas!!)
        //drawScale(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //Get size requested and size mode
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
        // if exactly, set width as desired and modify unit width to fill view
            MeasureSpec.EXACTLY -> widthSize
        // if wrap-content, uses attribute-specified unit width * units to determine
            MeasureSpec.AT_MOST -> {
                widthSize
            } // todo: !!! nie ma jeszcze osobnej obslugi wrapa
            else -> {
                widthSize
            }
        }

        val height = when (heightMode) {
        // if exactly, modify bar spacing and text size to fit
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> {
                val borders = borderSpacing * 2
                val bars = barHeight * events.size
                val spacings = barSpacing * events.size - 1
                borders + bars + spacings // todo: + top text + event titles
            }
            else -> {
                heightSize
            }
        }

        setMeasuredDimension(width, height)
    }

    private fun drawBars(canvas: Canvas) {
        val stopDateTime = stopDateTime()
        events.forEachIndexed { i, event ->
            val start = if (event.dtStart < startDateTime) {
                startDateTime
            } else {
                event.dtStart
            }

            val stop = if (event.dtStop > stopDateTime) {
                stopDateTime
            } else {
                event.dtStop
            }

            val startX = normForRange(start) * w
            val stopX = normForRange(stop) * w

            val Y = (borderSpacing + i * (barHeight + barSpacing) + barHeight / 2).toFloat()

            // Log.d("Timeline", "drawing: ($startX,$Y)->($stopX,$Y)")
            canvas.drawLine(startX, Y, stopX, Y, barPaint)
        }
    }

    /**
     *  todo: zdecydowanie zabójcze obliczeniowo (pętla po milisekundach)
     *  todo: trza by to jakoś inniej
     */
    private fun drawScale(canvas: Canvas) {
        val stopDateTime = stopDateTime()
        if (showScale || showTime) {
            for (i in startDateTime..stopDateTime) {
                val unitMillis = unitMillis(timelineUnit)
                if (showScale && i % (unitMillis * markScale).toLong() == 0L) {
                    // todo: draw scale
                    val x = normForRange(i) * w
                    canvas.drawLine(x, 0f, x, h.toFloat(), scalePaint)
                }
                if (showTime && i % (unitMillis * markTime).toLong() == 0L) {
                    // todo: draw time text
                }
            }
        }
    }
}
