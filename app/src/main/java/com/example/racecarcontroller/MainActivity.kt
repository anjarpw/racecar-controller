package com.example.racecarcontroller

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.racecarcontroller.ui.gamepad.GamepadFragment
import com.google.androidgamesdk.GameActivity


class MainActivity : GameActivity() {
    companion object {
        init {
            System.loadLibrary("racecarcontroller")
        }
    }

    private lateinit var keyDetector: GamepadKeyDetector


    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        viewModel = ViewModelProvider(this, MainActivityViewModelFactory()).get(
                MainActivityViewModel::class.java
            )
        setContentView(R.layout.main_activity)
        supportActionBar?.hide()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GamepadFragment.newInstance())
                .commitNow()
        }
        keyDetector = GamepadKeyDetector(viewModel.gamepad)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val isHandled = keyDetector.handleOnKeyDown(keyCode, event)
        if(isHandled){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val isHandled = keyDetector.handleOnKeyUp(keyCode, event)
        if(isHandled){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return keyDetector.handleOnGenericMotionEvent(event)
    }

    // Here we treat Button_A and DPAD_CENTER as the primary action
    // keys for the game.
    private fun isFireKey(keyCode: Int): Boolean =
        keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_BUTTON_A
}