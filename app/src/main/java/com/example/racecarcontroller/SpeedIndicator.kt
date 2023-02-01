package com.example.racecarcontroller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.racecarcontroller.ui.gamepad.GamepadState


class SpeedIndicator : View {

    private var _speed: Float = 0F
    private var _maxSpeed: Float = 0F

    fun updateSpeed(speed: Float, maxSpeed: Float) {
        _speed = speed
        _maxSpeed = maxSpeed
        this.invalidate()
    }

    private var resolver: DimensionResolver

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.resolver = DimensionResolver(this, RectF(0F, 0F, 10F, 10F))
        updateSpeed(10F, 40F)
    }


    val off: Paint = with(Paint()){
        color = ContextCompat.getColor(context, R.color.black)
        style = Paint.Style.STROKE
        isAntiAlias = true
        this
    }

    var on: Paint = with(Paint()){
        color = ContextCompat.getColor(context, R.color.red_engine)
        style = Paint.Style.STROKE
        isAntiAlias = true
        this
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val thickness = resolver.getNormalizedSize(0.5F, 1F)
        off.strokeWidth = thickness.y


        on.strokeWidth = thickness.x

        val oval = resolver.generateRect(0.5F, 0.5F, 9.5F, 9.5F)
        val myPath = Path()
        myPath.addArc(oval, 135F, 270F)
        val mySpeedPath = Path()
        mySpeedPath.addArc(oval, 135F, 270F * _speed / _maxSpeed)

        canvas.apply {
            canvas.drawPath(myPath, off)
            canvas.drawPath(mySpeedPath, on)
        }
    }
}
