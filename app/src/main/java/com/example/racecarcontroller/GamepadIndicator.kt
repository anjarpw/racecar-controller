package com.example.racecarcontroller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.racecarcontroller.ui.gamepad.GamepadState
import com.example.racecarcontroller.ui.gamepad.GamepadViewModel

class DimensionResolver(val view: View, val w: Float, val h: Float) {
    fun getNormalizedPosition(x: Float, y: Float): PointF {
        var newX = view.width * x / w
        var newY = view.height * y / h
        return PointF(newX, newY)
    }

    fun getNormalizedSize(x: Float, y: Float): PointF {
        var newX = view.width * x / w
        var newY = view.height * y / h
        return PointF(newX, newY)
    }
}

class GamepadIndicator : View {

    private var resolver: DimensionResolver

    private var _gamepad: GamepadState = GamepadState()

    fun updateGamepad(gamepad: GamepadState){
        _gamepad = gamepad
        this.invalidate()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.resolver = DimensionResolver(this, 7F, 3F)
    }

    fun toRect(center: PointF, dimension: PointF): RectF {
        return RectF(
            center.x - dimension.x / 2,
            center.y - dimension.y / 2,
            center.x + dimension.x / 2,
            center.y + dimension.y / 2
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var off = Paint()
        off.setColor(Color.parseColor("#000000"))
        off.setStyle(Paint.Style.FILL)
        off.setAntiAlias(true)

        var on = Paint()
        on.setColor(Color.parseColor("#FF0000"))
        on.setStyle(Paint.Style.FILL)
        on.setAntiAlias(true)

        var blockSize = this.resolver.getNormalizedSize(1F, 1F)
        var directionalButtonSize = this.resolver.getNormalizedSize(0.9F, 0.9F)
        val centerOffsideForDirectionalButtons = 0.15F
        var upPoint =
            this.resolver.getNormalizedPosition(1.5F, 0.5F + centerOffsideForDirectionalButtons)
        var leftPoint =
            this.resolver.getNormalizedPosition(0.5F + centerOffsideForDirectionalButtons, 1.5F)
        var rightPoint =
            this.resolver.getNormalizedPosition(2.5F - centerOffsideForDirectionalButtons, 1.5F)
        var downPoint =
            this.resolver.getNormalizedPosition(1.5F, 2.5F - centerOffsideForDirectionalButtons)

        val centerOffsideForActionButtons = 0.2F
        var yPoint = this.resolver.getNormalizedPosition(5.5F, 0.5F + centerOffsideForActionButtons)
        var xPoint = this.resolver.getNormalizedPosition(4.5F + centerOffsideForActionButtons, 1.5F)
        var bPoint = this.resolver.getNormalizedPosition(6.5F - centerOffsideForActionButtons, 1.5F)
        var aPoint = this.resolver.getNormalizedPosition(5.5F, 2.5F - centerOffsideForActionButtons)
        canvas.drawARGB(20, 0, 0, 0);

        canvas.apply {

            val roundedCorner = blockSize.x * 0.2F

            drawRoundRect(
                toRect(upPoint, directionalButtonSize),
                roundedCorner,
                roundedCorner,
                if (_gamepad.buttonUp) on else off
            )
            drawRoundRect(
                toRect(downPoint, directionalButtonSize),
                roundedCorner,
                roundedCorner,
                if (_gamepad.buttonDown) on else off
            )
            drawRoundRect(
                toRect(leftPoint, directionalButtonSize),
                roundedCorner,
                roundedCorner,
                if (_gamepad.buttonLeft) on else off
            )
            drawRoundRect(
                toRect(rightPoint, directionalButtonSize),
                roundedCorner,
                roundedCorner,
                if (_gamepad.buttonRight) on else off
            )

            drawCircle(xPoint.x, xPoint.y, blockSize.x / 2F, if (_gamepad.buttonX) on else off)
            drawCircle(yPoint.x, yPoint.y, blockSize.x / 2F, if (_gamepad.buttonY) on else off)
            drawCircle(aPoint.x, aPoint.y, blockSize.x / 2F, if (_gamepad.buttonA) on else off)
            drawCircle(bPoint.x, bPoint.y, blockSize.x / 2F, if (_gamepad.buttonB) on else off)
        }
    }
}
