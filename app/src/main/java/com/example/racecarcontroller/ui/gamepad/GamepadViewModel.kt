package com.example.racecarcontroller.ui.gamepad

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class GamepadState() {
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

class GamepadViewModel : ViewModel() {
    private val _state = MutableLiveData<GamepadState>()
    val state: LiveData<GamepadState>
        get() = _state

    fun setState(newState: GamepadState) {
        Log.d("GAMEPAD", newState.toString())
        _state.setValue(newState)
    }

    fun changeState(function: (GamepadState) -> Unit) {
        val newStateCopy: GamepadState = state.value?.copy() ?: GamepadState()
        function(newStateCopy)
        this.setState(newStateCopy)
    }
}