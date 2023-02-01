package com.example.racecarcontroller

import Scaler
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.Range
import android.view.View


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