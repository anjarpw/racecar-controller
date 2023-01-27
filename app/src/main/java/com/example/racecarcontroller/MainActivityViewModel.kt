package com.example.racecarcontroller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.racecarcontroller.ui.gamepad.GamepadFragment
import com.example.racecarcontroller.ui.gamepad.GamepadState
import com.example.racecarcontroller.ui.gamepad.GamepadViewModel

class MainActivityViewModel: ViewModel {
    val gamepad: GamepadViewModel

    constructor(){
        gamepad = GamepadViewModel()
    }

}

