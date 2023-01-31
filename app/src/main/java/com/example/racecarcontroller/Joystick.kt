package com.example.racecarcontroller

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.Range
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.RelativeLayout
import com.example.racecarcontroller.databinding.JoystickBinding
import java.util.*

class Joystick(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {


    fun setRange(range: Range<Float>, isInverted: Boolean = false) {
        scaler.virtualRange = range
        scaler.isInverted = isInverted
    }

    fun configureAnchoring(anchor: Float, anchorTolerance: Float = 1F, feedbackSpeed: Float = 0.2F) {
        this.anchor = anchor
        this.anchorTolerance = anchorTolerance
        this.feedbackSpeed = feedbackSpeed
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
        this.anchor = anchor
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
    private var anchor: Float = 0F
    private var anchorTolerance: Float = 1F
    private var feedbackSpeed = 0.2F


    private fun isAtAnchor(p: Float): Boolean {
        return Math.abs(p - anchor) < anchorTolerance
    }

    private fun pullToAnchor(newPosition: Float): Float {
        val diff = (anchor - newPosition) * feedbackSpeed
        var p = diff + newPosition
        if (isAtAnchor(p)) {
            p = anchor
        }
        return p
    }


    private fun setPosition(newPosition: Float) {
        var p = newPosition
        val range = scaler.virtualRange
        if (p < range.lower) {
            p = range.lower
        } else if (p > range.upper) {
            p = range.upper
        }
        position = p
    }

    init {
        val acquiredAttributes = context.obtainStyledAttributes(attrs, R.styleable.Joystick)
        anchor = acquiredAttributes.getFloat(R.styleable.Joystick_anchor, 0F)
        val rangeFrom = acquiredAttributes.getFloat(R.styleable.Joystick_rangeFrom, 0F)
        val rangeTo = acquiredAttributes.getFloat(R.styleable.Joystick_rangeTo, 100F)
        val isInverted = acquiredAttributes.getBoolean(R.styleable.Joystick_isInverted, false)
        val orientation = acquiredAttributes.getInt(R.styleable.Joystick_orientation, 1)
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
        Log.i("JOYSTICK", "${position}")
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
            binding.thumb.post {
                setPosition(scaler.toVirtual(relativePosition))
                renderPosition()
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        renderPosition()
    }
    private fun renderPosition() {
        val buttonSize = getButtonSize()

        var newViewPosition = scaler.toView(position)
        val layoutParams = LayoutParams(binding.thumb.layoutParams)
        if(buttonSize > 0){
            layoutParams.width = buttonSize
            layoutParams.height = buttonSize
        }
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
                    return
                }
                if (isAtAnchor(position)) {
                    return
                }

                binding.thumb.post {
                    val p = pullToAnchor(position)
                    setPosition(p)
                    renderPosition()
                }
            }
        }, 0, 50)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }
}
