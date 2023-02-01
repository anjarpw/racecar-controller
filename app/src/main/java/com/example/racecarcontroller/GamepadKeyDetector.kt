package com.example.racecarcontroller

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import com.example.racecarcontroller.ui.gamepad.GamepadViewModel

class GamepadKeyDetector(gamepadViewModel: GamepadViewModel) {

    val gamepadViewModel = gamepadViewModel
    val timeLooper: TimeLooper

    init{
        timeLooper = TimeLooper(50L){
            val indicator = gamepadViewModel.getCachedIndicatorValue()
            if(indicator.buttonB){
                gamepadViewModel.throttleBody.forceThrottle(-20F)
            }
            if(indicator.buttonA){
                gamepadViewModel.throttleBody.forceThrottle(100F)
            }
            if(indicator.buttonLeft){
                gamepadViewModel.throttleBody.forceDirection(-100F)
            }
            if(indicator.buttonRight){
                gamepadViewModel.throttleBody.forceDirection(100F)
            }
        }
        timeLooper.ensureTimerStart()
    }

    fun getDirectionPressed(event: MotionEvent): Int {
        if (!isDpadDevice(event)) {
            return -1
        }
        val xaxis: Float = event.getAxisValue(MotionEvent.AXIS_HAT_X)
        val yaxis: Float = event.getAxisValue(MotionEvent.AXIS_HAT_Y)

        val directionPressed = when {
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
    fun handleOnGenericMotionEvent(event: MotionEvent): Boolean {
        var isHandled = false
        if (GamepadKeyDetector.isDpadDevice(event)) {
            isHandled = true
            when (this.getDirectionPressed(event)) {
                GamepadKeyDetector.LEFT -> {
                    gamepadViewModel.changeIndicator { x -> x.buttonLeft = true }
                    gamepadViewModel.throttleBody.forceDirection(-100F)
                    // Do something for LEFT direction press
                }
                GamepadKeyDetector.RIGHT -> {
                    gamepadViewModel.changeIndicator { x -> x.buttonRight = true }
                    gamepadViewModel.throttleBody.forceDirection(100F)
                    // Do something for RIGHT direction press
                }
                GamepadKeyDetector.UP -> {
                    gamepadViewModel.changeIndicator { x -> x.buttonUp = true }
                    // Do something for UP direction press
                }
                GamepadKeyDetector.DOWN -> {
                    gamepadViewModel.changeIndicator { x -> x.buttonDown = true }
                    // Do something for UP direction press
                }
                GamepadKeyDetector.CENTER -> {
                    gamepadViewModel.changeIndicator { x ->
                        run {
                            x.buttonUp = false
                            x.buttonDown = false
                            x.buttonLeft = false
                            x.buttonRight = false
                        }
                    }
                    isHandled = false
                }
            }

        }
        return isHandled
    }


    fun handleOnKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        var isHandled = false
        if ((event.source and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (event.repeatCount == 0) {
                when (keyCode) {
                    KeyEvent.KEYCODE_BUTTON_A -> {
                        gamepadViewModel.changeIndicator { x -> x.buttonA = false }
                        isHandled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_B -> {
                        gamepadViewModel.changeIndicator { x -> x.buttonB = false }
                        isHandled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_X -> {
                        gamepadViewModel.changeIndicator { x -> x.buttonX = false }
                        isHandled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_Y -> {
                        gamepadViewModel.changeIndicator { x -> x.buttonY = false }
                        isHandled = true
                    }
                }
            }
        }
        return isHandled
    }

    fun handleOnKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        var isHandled = false
        if ((event.source and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (event.repeatCount == 0) {
                when (keyCode) {
                    KeyEvent.KEYCODE_BUTTON_A -> {
                        gamepadViewModel.changeIndicator { x -> x.buttonA = true }
                        gamepadViewModel.throttleBody.forceThrottle(100F)
                        isHandled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_B -> {
                        gamepadViewModel.changeIndicator { x -> x.buttonB = true }
                        gamepadViewModel.throttleBody.forceThrottle(-20F)
                        isHandled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_X -> {
                        gamepadViewModel.changeIndicator { x -> x.buttonX = true }
                        isHandled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_Y -> {
                        gamepadViewModel.changeIndicator { x -> x.buttonY = true }
                        isHandled = true
                    }
                }
            }
        }
        return isHandled
    }

    companion object {
        internal const val UP = 0
        internal const val LEFT = 1
        internal const val RIGHT = 2
        internal const val DOWN = 3
        internal const val CENTER = 4

        fun isDpadDevice(event: MotionEvent): Boolean {
            // Check that input comes from a device with directional pads.
            return (event.source and InputDevice.SOURCE_DPAD) != InputDevice.SOURCE_DPAD
        }
    }
}