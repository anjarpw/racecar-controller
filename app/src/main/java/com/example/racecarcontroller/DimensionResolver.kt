package com.example.racecarcontroller

import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.Range
import android.view.View

class Scaler(
    virtualRange: Range<Float>,
    getViewRange: () -> Range<Int>,
    isInverted: Boolean = false
) {

    var virtualRange = virtualRange
    val getViewRange = getViewRange
    var isInverted = isInverted

    private fun getVirtualFrom(): Float {
        return if (isInverted) virtualRange.upper else virtualRange.lower
    }

    private fun getVirtualTo(): Float {
        return if (isInverted) virtualRange.lower else virtualRange.upper
    }

    fun updateVirtualRange(virtualRange: Range<Float>, isInverted: Boolean) {
        this.virtualRange = virtualRange
        this.isInverted = isInverted
    }

    private fun getScaleViewToVirtual(viewRange: Range<Int>): Float {
        return (viewRange.upper - viewRange.lower) / (getVirtualTo() - getVirtualFrom())
    }

    private fun getScaleVirtualToView(viewRange: Range<Int>): Float {
        return (getVirtualTo() - getVirtualFrom()) / (viewRange.upper - viewRange.lower)
    }

    fun toView(virtualValue: Float): Float {
        val viewRange = getViewRange()
        return (virtualValue - getVirtualFrom()) * getScaleViewToVirtual(viewRange) + viewRange.lower
    }

    fun toVirtual(viewValue: Float): Float {
        val viewRange = getViewRange()
        return (viewValue - viewRange.lower) * getScaleVirtualToView(viewRange) + getVirtualFrom()
    }
}

class DimensionResolver(
    val view: View,
    rect: RectF,
    isXInverted: Boolean = false,
    isYInverted: Boolean = false
) {

    private val horizontalScaler: Scaler = Scaler(Range(rect.left, rect.right), {
        Range(
            0,
            view.width
        )
    }, isXInverted)
    private val verticalScaler: Scaler = Scaler(Range(rect.top, rect.bottom), {
        Range(
            0,
            view.height
        )
    }, isYInverted)

    fun getNormalizedPosition(x: Float, y: Float): PointF {
        var newX = horizontalScaler.toView(x)
        var newY = verticalScaler.toView(y)
        return PointF(newX, newY)
    }

    fun getNormalizedSize(x: Float, y: Float): PointF {
        var newX = horizontalScaler.toView(x)
        var newY = verticalScaler.toView(y)
        return PointF(newX, newY)
    }

    fun normalizeRect(rect: RectF): RectF {
        val topLeft = getNormalizedSize(rect.left, rect.top)
        val rightBottom = getNormalizedSize(rect.right, rect.bottom)
        return RectF(topLeft.x, topLeft.y, rightBottom.x, rightBottom.y)
    }

    fun generateRect(left: Float, top: Float, right: Float, bottom: Float): RectF {
        val topLeft = getNormalizedSize(left, top)
        val rightBottom = getNormalizedSize(right, bottom)
        return RectF(topLeft.x, topLeft.y, rightBottom.x, rightBottom.y)
    }
}