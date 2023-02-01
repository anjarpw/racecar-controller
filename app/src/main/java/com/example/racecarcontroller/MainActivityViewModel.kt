package com.example.racecarcontroller

import android.util.Range
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.racecarcontroller.ui.gamepad.GamepadFragment
import com.example.racecarcontroller.ui.gamepad.GamepadState
import com.example.racecarcontroller.ui.gamepad.GamepadViewModel

class MainActivityViewModel: ViewModel {
    val gamepad: GamepadViewModel

    constructor(gamepad: GamepadViewModel){
        this.gamepad = gamepad
    }
}
class MainActivityViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val throttleBody = ThrottleBodyViewModel(Range(0F,100F), Range(-100F, 100F))
        val gamepadViewModel = GamepadViewModel(throttleBody)
        return MainActivityViewModel(gamepadViewModel) as T
    }
}

