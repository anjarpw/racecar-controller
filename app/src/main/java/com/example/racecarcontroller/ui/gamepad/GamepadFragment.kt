package com.example.racecarcontroller.ui.gamepad

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.racecarcontroller.MainActivityViewModel
import com.example.racecarcontroller.databinding.GamepadFragmentBinding

class GamepadFragment : Fragment() {

    companion object {
        fun newInstance() = GamepadFragment()
    }

    private lateinit var viewModel: GamepadViewModel
    private lateinit var parentViewModel: MainActivityViewModel


    private var binding: GamepadFragmentBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        viewModel = parentViewModel.gamepad
        setHasOptionsMenu(false)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = GamepadFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }
    var gameControllerDeviceIds: MutableList<Int>? = null

    fun getGameControllerIds(): MutableList<Int> {
        val list = mutableListOf<Int>()
        val deviceIds = InputDevice.getDeviceIds()
        deviceIds.forEach { deviceId ->
            InputDevice.getDevice(deviceId).apply {

                // Verify that the device has gamepad buttons, control sticks, or both.
                if (sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD
                    || sources and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK) {
                    // This device is a game controller. Store its device ID.
                    list
                        .takeIf { !it.contains(deviceId) }
                        ?.add(deviceId)
                }
            }
        }
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val b = binding!!
        viewModel.state.observe(viewLifecycleOwner, Observer<GamepadState> { gamepad: GamepadState? ->
            b.gamepadIndicator.updateGamepad(gamepad!!)
            b.text.text = gamepad.toString()
            Log.d("GAMEPAD OBSERVED", gamepad.toString())
        })

        b.buttonA.setOnClickListener {
            viewModel.changeState { x -> x.buttonA = !x.buttonA }
        }
        b.buttonB.setOnClickListener {
            viewModel.changeState { x -> x.buttonB = !x.buttonB }
        }
        b.buttonX.setOnClickListener {
            viewModel.changeState { x -> x.buttonX = !x.buttonX }
        }
        b.refreshDevice.setOnClickListener {
            gameControllerDeviceIds = getGameControllerIds()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}