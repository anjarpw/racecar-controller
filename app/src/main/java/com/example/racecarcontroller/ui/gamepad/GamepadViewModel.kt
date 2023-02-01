package com.example.racecarcontroller.ui.gamepad

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.racecarcontroller.MutableWithCache
import com.example.racecarcontroller.ThrottleBodyViewModel


class GamepadState {
    var buttonA = false
    var buttonB = false
    var buttonX = false
    var buttonY = false
    var buttonLeft = false
    var buttonRight = false
    var buttonUp = false
    var buttonDown = false
    override fun toString(): String{
        return "A:"+buttonA+",B:"+buttonB+"X:"+buttonX+",Y:"+buttonY+",L:"+buttonLeft+",R:"+buttonRight+",U:"+buttonUp+",D:"+buttonDown
    }
    fun copy(): GamepadState{
        val x = GamepadState()
        x.buttonA = buttonA
        x.buttonB = buttonB
        x.buttonX = buttonX
        x.buttonY = buttonY
        x.buttonUp = buttonUp
        x.buttonDown= buttonDown
        x.buttonLeft = buttonLeft
        x.buttonRight = buttonRight
        return x
    }
}

class GamepadViewModel(throttleBody: ThrottleBodyViewModel) : ViewModel() {
    private val cachedIndicator = MutableWithCache<GamepadState>(GamepadState())
    val indicator: LiveData<GamepadState>
        get() = cachedIndicator.liveData

    val throttleBody: ThrottleBodyViewModel = throttleBody
    fun setIndicator(newState: GamepadState) {
        Log.d("GAMEPAD", newState.toString())
        cachedIndicator.postValue(newState)
    }

    fun changeIndicator(function: (GamepadState) -> Unit) {
        val newStateCopy: GamepadState = cachedIndicator.cachedValue?.copy() ?: GamepadState()
        function(newStateCopy)
        this.setIndicator(newStateCopy)
    }
}