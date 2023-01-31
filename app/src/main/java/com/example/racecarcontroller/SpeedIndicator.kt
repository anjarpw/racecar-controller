package com.example.racecarcontroller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.racecarcontroller.ui.gamepad.GamepadState


class SpeedIndicator : View {

    private var _speed: Float = 0F
    private var _maxSpeed: Float = 0F

    fun updateSpeed(speed: Float, maxSpeed: Float){
        _speed = speed
        _maxSpeed = maxSpeed
        this.invalidate()
    }
    private var resolver: DimensionResolver

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.resolver = DimensionResolver(this, RectF(0F, 0F,10F, 10F))
        updateSpeed(10F,40F)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var thickness = resolver.getNormalizedSize(0.5F,1F)
        var off = Paint()
        off.color = resources.getColor(R.color.black)
        off.style = Paint.Style.STROKE
        off.isAntiAlias = true
        off.strokeWidth = thickness.y


        var on = Paint()
        on.color = resources.getColor(R.color.red_engine)
        on.style = Paint.Style.STROKE
        on.isAntiAlias = true
        on.strokeWidth = thickness.x

        val oval = resolver.generateRect(0.5F,0.5F,9.5F,9.5F)
        val myPath = Path()
        myPath.addArc(oval, 135F, 270F)
        val mySpeedPath = Path()
        mySpeedPath.addArc(oval, 135F, 270F*_speed/_maxSpeed)

        canvas.apply {
            canvas.drawPath(myPath, off)
            canvas.drawPath(mySpeedPath, on)
        }
    }
}
