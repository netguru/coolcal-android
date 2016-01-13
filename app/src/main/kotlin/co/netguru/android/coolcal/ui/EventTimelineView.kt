package co.netguru.android.coolcal.ui

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import co.netguru.android.coolcal.R
import co.netguru.android.coolcal.model.Event

class EventTimelineView : View {

    companion object {
        const val DEFAULT_UNITS = 24
        const val DEFAULT_UNIT_WIDTH = 8 // px
        const val DEFAULT_BAR_HEIGHT = 6 // px
        const val DEFAULT_BAR_SPACING = 6 // px
        const val DEFAULT_SCALE_COLOR = Color.LTGRAY

        val linePaint: Paint by lazy {
            val paint = Paint()
            paint.color = Color.RED
            paint.isAntiAlias = true
            paint.strokeCap = Paint.Cap.ROUND
            paint.strokeWidth = 4f
            paint
        }
    }

    private var units = DEFAULT_UNITS
    private var _events = emptyList<Event>()
    public var events: List<Event>
        get() = _events
        set(value) {
            _events = value
        }
    private var timelineUnitWidth: Int = DEFAULT_UNIT_WIDTH
    private var timelineBarHeight: Int = DEFAULT_BAR_HEIGHT
    private var timelineBarSpacing: Int = DEFAULT_BAR_SPACING
    private var scaleColor: Int = DEFAULT_SCALE_COLOR

    private var w = 0
    private var h = 0

    private var _minDt = 0L
    public var minDt: Long
        get() = _minDt
        set(value) {
            _minDt = value
        }
    private var _maxDt = Long.MAX_VALUE
    public var maxDt: Long
        get() = _maxDt
        set(value) {
            _maxDt = value
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
        val a = context.obtainStyledAttributes(attrs, R.styleable.EventTimelineView,
                defStyleAttr, defStyleRes)
        for (i in 0..a.indexCount) {
            val attr = a.getIndex(i)
            when (attr) {
                R.styleable.EventTimelineView_hourSpan -> units =
                        a.getInt(attr, DEFAULT_UNITS)
                R.styleable.EventTimelineView_scaleColor -> scaleColor =
                        a.getColor(attr, DEFAULT_SCALE_COLOR)
                R.styleable.EventTimelineView_barHeight -> timelineBarHeight =
                        a.getDimensionPixelSize(attr, DEFAULT_BAR_HEIGHT)
                R.styleable.EventTimelineView_unitWidth -> timelineUnitWidth =
                        a.getDimensionPixelSize(attr, DEFAULT_UNIT_WIDTH)
                R.styleable.EventTimelineView_barSpacing -> timelineBarSpacing =
                        a.getDimensionPixelSize(attr, DEFAULT_BAR_SPACING)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

        Log.i("Timeline", "Size = $w x $h")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        fun normForRange(value: Long) = (value - minDt).toFloat() / (maxDt - minDt)

        events.forEachIndexed { i, event ->
            val start = if (event.dtStart < minDt) minDt else event.dtStart
            val stop = if (event.dtStop > maxDt) maxDt else event.dtStop
            val startX = normForRange(start) * w
            val stopX = normForRange(stop) * w
            val Y = (2 + (i * 8)).toFloat()

            Log.d("Timeline", "drawing: ($startX,$Y)->($stopX,$Y)")
            canvas!!.drawLine(startX, Y, stopX, Y, linePaint)
        }
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        //Get size requested and size mode
//        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
//        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
//        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
//        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
//
//        //Determine Width:
//        val width = when (widthMode) {
//        // if exactly, set width as desired and modify unit width to fill view
//            MeasureSpec.EXACTLY -> {
//                timelineUnitWidth = widthSize / units
//                widthSize
//            }
//        // if wrap-content, uses attribute-specified unit width * units to determine
//            MeasureSpec.AT_MOST -> units * timelineUnitWidth
//            else -> {
//                widthSize
//            }
//        }
//
//        //Determine Height: no default, wrap content and static bar height * bars
//        val height = when (heightMode) {
//        // todo: if exactly, modify bar height and text size to fit
//            MeasureSpec.EXACTLY -> heightSize
//        // todo: if wrap-content, use attr-specified bar height and textsize to determine final height
//            MeasureSpec.AT_MOST -> throw UnsupportedOperationException()
//            else -> {
//                heightSize
//            } // careful
//        }
//
//        setMeasuredDimension(width, height)
//    }
}
