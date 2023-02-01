package com.example.racecarcontroller

import Scaler
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Range
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.example.racecarcontroller.databinding.JoystickBinding
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

private const val TimerDuration = 50L

class Joystick(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {


    fun setRange(range: Range<Float>, isInverted: Boolean = false) {
        scaler.virtualRange = range
        scaler.isInverted = isInverted
    }

    fun configureAnchoring(anchor: Float,  feedbackSpeed: Float = 0.2F, anchorTolerance: Float = 1F) {
        anchoring.update(anchor, feedbackSpeed, anchorTolerance)
    }

    fun getPosition(): Float {
        return position
    }

    fun setRangeFrom(rangeFrom: Float) {
        setRange(Range(rangeFrom, scaler.virtualRange.upper), scaler.isInverted)
        invalidate()
    }
    fun setRangeTo(rangeTo: Float) {
        setRange(Range(scaler.virtualRange.lower, rangeTo), scaler.isInverted)
        invalidate()
    }

    fun setAnchor(anchor: Float){
        configureAnchoring(anchor)
        invalidate()
    }

    fun setIsInverted(isInverted: Boolean) {
        setRange(scaler.virtualRange, isInverted)
    }

    private var isDragging = false
    private lateinit var binding: JoystickBinding
    private var timer: Timer? = null

    private lateinit var scaler: Scaler
    private var isVertical = true
    private var position: Float = 0F
    private lateinit var anchoring: Anchoring


    private fun setPosition(newPosition: Float) {
        position = Scaler.clip(scaler.virtualRange, newPosition)
    }

    init {
        val acquiredAttributes = context.obtainStyledAttributes(attrs, R.styleable.Joystick)
        var anchor = acquiredAttributes.getFloat(R.styleable.Joystick_anchor, 0F)
        val rangeFrom = acquiredAttributes.getFloat(R.styleable.Joystick_rangeFrom, 0F)
        val rangeTo = acquiredAttributes.getFloat(R.styleable.Joystick_rangeTo, 100F)
        val isInverted = acquiredAttributes.getBoolean(R.styleable.Joystick_isInverted, false)
        val orientation = acquiredAttributes.getInt(R.styleable.Joystick_orientation, 1)
        anchoring = Anchoring(anchor,  0.2F,  1F)
        isVertical = orientation == 1
        binding = JoystickBinding.inflate(LayoutInflater.from(context), this, true)
        scaler = Scaler(Range(rangeFrom, rangeTo),  {
            if(isVertical){
                return@Scaler Range(binding.thumb.height / 2, this.height - binding.thumb.height / 2)
            }else{
                return@Scaler Range(binding.thumb.width / 2, this.width - binding.thumb.width / 2)
            }
        }, isInverted)
        startTimer()
        binding.thumb.setOnTouchListener { v, event ->
            handlePosition(event)
            true
        }
        acquiredAttributes.recycle()
        setPosition(anchor)
    }

    private fun getButtonSize(): Int {
        if(isVertical){
            return this.width.toInt()
        }else{
            return this.height.toInt()
        }
    }

    private fun handlePosition(event: MotionEvent) {
        var relativePosition = 0F
        if(isVertical){
            relativePosition = binding.thumb.y + event.y
        }else{
            relativePosition = binding.thumb.x + event.x
        }
        var isAdjustingPosition = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDragging = true
                isAdjustingPosition = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    isAdjustingPosition = true
                }
            }
            MotionEvent.ACTION_UP -> {
                isDragging = false
                isAdjustingPosition = true
            }
        }
        if (isAdjustingPosition) {
            setPosition(scaler.toVirtual(relativePosition))
            emitPositionEvent(true)
            binding.thumb.post {
                renderPosition()
            }
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        renderPosition()
    }


    private fun drawBackground(){
        val buttonSize = getButtonSize()
        val w = this.width
        val h = this.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val thickness = 50F
        val padding = (buttonSize-thickness)/2
        val background: Paint = with(Paint()){
            color = ContextCompat.getColor(context, R.color.black)
            style = Paint.Style.FILL
            isAntiAlias = true
            setAlpha(32)
            this
        }
        val line: Paint = with(Paint()){
            color = ContextCompat.getColor(context, R.color.black)
            style = Paint.Style.FILL
            isAntiAlias = true
            setAlpha(128)
            this
        }
        val activeLine: Paint = with(Paint()){
            color = ContextCompat.getColor(context, R.color.red_engine)
            style = Paint.Style.FILL
            isAntiAlias = true
            this
        }
        val dash: Paint = with(Paint()){
            color = ContextCompat.getColor(context, R.color.red_body)
            style = Paint.Style.FILL
            isAntiAlias = true
            setAlpha(40)
            this
        }
        val viewPosition = scaler.toView(position)
        val viewAnchor = scaler.toView(anchoring.anchor)
        canvas.drawRoundRect(0F, 0F, w.toFloat(), h.toFloat(), buttonSize.toFloat()/2, buttonSize.toFloat()/2, background)
        canvas.drawRoundRect(padding, padding, w.toFloat()-padding, h.toFloat()-padding, thickness/2, thickness/2, line)
        var t = thickness/2
        if(viewPosition>viewAnchor){
            t = -thickness/2
        }
        if(isVertical){
            canvas.drawRect(0F, viewPosition, buttonSize.toFloat(), viewAnchor, dash)
            canvas.drawRoundRect(padding, viewPosition-t, w.toFloat()-padding, viewAnchor+t, thickness/2, thickness/2, activeLine)
        }else{
            canvas.drawRect(viewPosition, 0F,viewAnchor,  buttonSize.toFloat(), dash)
            canvas.drawRoundRect(viewPosition-t, padding, viewAnchor+t, h.toFloat()-padding, thickness/2, thickness/2, activeLine)
        }


        binding.bg.setImageBitmap(bmp)
    }

    var isFirstRendered = false
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if(!isFirstRendered){
            renderPosition()
            isFirstRendered = true
        }
    }

    private var lastEmittedPositionEvent: LocalDateTime  =  LocalDateTime.now()
    private fun emitPositionEvent(isForced: Boolean){
        val now =  LocalDateTime.now()
        val duration = Duration.between(lastEmittedPositionEvent, now).abs()
        if(!isForced && duration.toMillis() < TimerDuration){
             return
        }
         listener?.onJoystickMoved(this, position)
        lastEmittedPositionEvent = now
    }
    private fun renderPosition() {
        val buttonSize = getButtonSize()
        drawBackground()
        binding.thumb.text = ""
        var newViewPosition = scaler.toView(position)
        val layoutParams = LayoutParams(binding.thumb.layoutParams)
        layoutParams.width = buttonSize
        layoutParams.height = buttonSize
        if(isVertical) {
            layoutParams.topMargin = newViewPosition.toInt() - buttonSize / 2
        }else{
            layoutParams.leftMargin = newViewPosition.toInt() - buttonSize / 2
        }
        binding.thumb.layoutParams = layoutParams
        invalidate()
    }


    private fun startTimer() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (isDragging) {
                    emitPositionEvent(false)
                    return
                }
                if (anchoring.isAtAnchor(position)) {
                    return
                }

                binding.thumb.post {
                    val p = anchoring.pullToAnchor(position)
                    setPosition(p)
                    renderPosition()
                }

            }
        }, 0, TimerDuration)
    }


    private var listener: OnJoystickListener? = null
    fun setOnJoystickListener(listener: OnJoystickListener) {
        this.listener = listener
    }

}
interface OnJoystickListener {
    fun onJoystickMoved(view: View, position: Float)
}

