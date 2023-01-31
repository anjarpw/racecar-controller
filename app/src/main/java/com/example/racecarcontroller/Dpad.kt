package com.example.racecarcontroller

import android.view.InputDevice
import android.view.MotionEvent

class Dpad {


    private var directionPressed = -1 // initialized to -1

    fun getDirectionPressed(event: MotionEvent): Int {
        if (!isDpadDevice(event)) {
            return -1
        }
        val xaxis: Float = event.getAxisValue(MotionEvent.AXIS_HAT_X)
        val yaxis: Float = event.getAxisValue(MotionEvent.AXIS_HAT_Y)

        directionPressed = when {
            // Check if the AXIS_HAT_X value is -1 or 1, and set the D-pad
            // LEFT and RIGHT direction accordingly.
            xaxis.compareTo(-1.0f) == 0 -> LEFT
            xaxis.compareTo(1.0f) == 0 -> RIGHT
            // Check if the AXIS_HAT_Y value is -1 or 1, and set the D-pad
            // UP and DOWN direction accordingly.
            yaxis.compareTo(-1.0f) == 0 -> UP
            yaxis.compareTo(1.0f) == 0 -> DOWN
            else -> CENTER
        }

        return directionPressed
    }

    companion object {
        internal const val UP = 0
        internal const val LEFT = 1
        internal const val RIGHT = 2
        internal const val DOWN = 3
        internal const val CENTER = 4

        fun isDpadDevice(event: MotionEvent): Boolean =
            // Check that input comes from a device with directional pads.
            (event.source and InputDevice.SOURCE_DPAD) != InputDevice.SOURCE_DPAD
    }
}