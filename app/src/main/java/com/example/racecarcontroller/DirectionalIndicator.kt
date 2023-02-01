package com.example.racecarcontroller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat


class DirectionalIndicator : View {

    private var _direction: Float = 0F
    fun updateDirection(direction: Float) {
        _direction = direction
        this.invalidate()
    }

    var resolver: DimensionResolver
        private set

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.resolver = DimensionResolver(this, RectF(0F, 0F, 10F, 2F))
        updateDirection(-0.5F)
    }

    private val thinLine: Paint = with(Paint()){
        color = ContextCompat.getColor(context, R.color.black)
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = 4F
        this
    }

    private val on: Paint = with(Paint()){
        color = ContextCompat.getColor(context, R.color.red_engine)
        style = Paint.Style.FILL
        isAntiAlias = true
        this
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val baseLine = 1.2F
        val bigScale = 2F
        val midScale = 1.6F
        val smallScale = 1.6F
        val horizontalLines = resolver.generateRect(1F, baseLine, 9F, baseLine)
        val vL4Dots = resolver.generateRect(1F, bigScale, 1F, baseLine)
        val vL3Dots = resolver.generateRect(2F, smallScale, 2F, baseLine)
        val vL2Dots = resolver.generateRect(3F, midScale, 3F, baseLine)
        val vL1Dots = resolver.generateRect(4F, smallScale, 4F, baseLine)
        val vMidDots = resolver.generateRect(5F, bigScale, 5F, baseLine)
        val vR1Dots = resolver.generateRect(6F, smallScale, 6F, baseLine)
        val vR2Dots = resolver.generateRect(7F, midScale, 7F, baseLine)
        val vR3Dots = resolver.generateRect(8F, smallScale, 8F, baseLine)
        val vR4Dots = resolver.generateRect(9F, bigScale, 9F, baseLine)


        val triangle = Path()
        val pivotX = 4F * _direction + 5F
        val a = resolver.getNormalizedPosition(pivotX, baseLine)
        val b = resolver.getNormalizedPosition(pivotX - 0.5F, 0F)
        val c = resolver.getNormalizedPosition(pivotX + 0.5F, 0F)

        triangle.moveTo(a.x, a.y)
        triangle.lineTo(b.x, b.y)
        triangle.lineTo(c.x, c.y)
        triangle.lineTo(a.x, a.y)
        triangle.close()

        canvas.apply {
            val drawLineOnCanvas = { line: RectF ->
                run {
                    canvas.drawLine(line.left, line.top, line.right, line.bottom, thinLine)
                }
            }
            drawLineOnCanvas(horizontalLines)
            drawLineOnCanvas(vL4Dots)
            drawLineOnCanvas(vL3Dots)
            drawLineOnCanvas(vL2Dots)
            drawLineOnCanvas(vL1Dots)
            drawLineOnCanvas(vMidDots)
            drawLineOnCanvas(vR1Dots)
            drawLineOnCanvas(vR2Dots)
            drawLineOnCanvas(vR3Dots)
            drawLineOnCanvas(vR4Dots)
            canvas.drawPath(triangle, on)
        }
    }
}
