package com.example.racecarcontroller

import android.os.Bundle
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.racecarcontroller.ui.gamepad.GamepadFragment
import com.google.androidgamesdk.GameActivity


class MainActivity : GameActivity() {
    companion object {
        init {
            System.loadLibrary("racecarcontroller")
        }
    }

    private val dpad = Dpad()


    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        setContentView(R.layout.main_activity)
        supportActionBar?.hide()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GamepadFragment.newInstance())
                .commitNow()
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        var handled = false
        if ((event.source and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (event.repeatCount == 0) {
                when (keyCode) {
                    KeyEvent.KEYCODE_BUTTON_A -> {
                        viewModel.gamepad.changeState { x -> x.buttonA = true }
                        handled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_B -> {
                        viewModel.gamepad.changeState { x -> x.buttonB = true }
                        handled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_X -> {
                        viewModel.gamepad.changeState { x -> x.buttonX = true }
                        handled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_Y -> {
                        viewModel.gamepad.changeState { x -> x.buttonY = true }
                        handled = true
                    }
                }
            }
            if (handled) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        var handled = false
        if ((event.source and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (event.repeatCount == 0) {
                when (keyCode) {
                    KeyEvent.KEYCODE_BUTTON_A -> {
                        viewModel.gamepad.changeState { x -> x.buttonA = false }
                        handled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_B -> {
                        viewModel.gamepad.changeState { x -> x.buttonB = false }
                        handled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_X -> {
                        viewModel.gamepad.changeState { x -> x.buttonX = false }
                        handled = true
                    }
                    KeyEvent.KEYCODE_BUTTON_Y -> {
                        viewModel.gamepad.changeState { x -> x.buttonY = false }
                        handled = true
                    }
                }
            }
            if (handled) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        if (Dpad.isDpadDevice(event)) {
            when (dpad.getDirectionPressed(event)) {
                Dpad.LEFT -> {
                    viewModel.gamepad.changeState { x -> x.buttonLeft = true }
                    // Do something for LEFT direction press
                }
                Dpad.RIGHT -> {
                    viewModel.gamepad.changeState { x -> x.buttonRight = true }
                    // Do something for RIGHT direction press
                }
                Dpad.UP -> {
                    viewModel.gamepad.changeState { x -> x.buttonUp = true }
                    // Do something for UP direction press
                }
                Dpad.DOWN -> {
                    viewModel.gamepad.changeState { x -> x.buttonDown = true }
                    // Do something for UP direction press
                }
                Dpad.CENTER -> {
                    viewModel.gamepad.changeState { x ->
                        run {
                            x.buttonUp = false
                            x.buttonDown = false
                            x.buttonLeft = false
                            x.buttonRight = false
                        }
                    }
                }
            }
        }
        return false
    }

    // Here we treat Button_A and DPAD_CENTER as the primary action
    // keys for the game.
    private fun isFireKey(keyCode: Int): Boolean =
        keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_BUTTON_A
}