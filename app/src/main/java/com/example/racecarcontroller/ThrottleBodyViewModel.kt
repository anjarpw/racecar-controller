package com.example.racecarcontroller

import Scaler
import android.util.Log
import android.util.Range
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

private const val TimerDuration = 50L
private const val GracePeriodDuration = 100L

class ThrottleBodyViewModel(trottleRange: Range<Float>, directionalRange: Range<Float>): ViewModel() {
    private val trottleRange = trottleRange
    private val directionalRange = directionalRange
    private val cachedDirection = MutableWithCache<Float>(0F)
    private val cachedSpeed = MutableWithCache<Float>(0F)

    private val directionalAnchoring = Anchoring(0F, 0.005F, 1F)
    private val throttleAnchoring = Anchoring(0F, 0.005F, 1F)
    private val responsivePuller = Puller(0.1F, 1F)

    private val timeLooper: TimeLooper

    val direction: LiveData<Float>
        get() = cachedDirection.liveData

    val speed: LiveData<Float>
        get() = cachedSpeed.liveData


    var lastForcingTrottleTime: LocalDateTime = LocalDateTime.now()
    var lastForcingDirectionTime: LocalDateTime = LocalDateTime.now()
    fun forceDirection(d: Float){
        var d = responsivePuller.pullToSomewhere(cachedDirection.cachedValue!!, d)
        d =  Scaler.clip(directionalRange, d)
        Log.i("JOYSTICK", "direction ${d}")
        cachedDirection.postValue(d)
        lastForcingTrottleTime = LocalDateTime.now()
    }
    fun forceThrottle(t: Float){
        var t = responsivePuller.pullToSomewhere(cachedSpeed.cachedValue!!, t)
        t =  Scaler.clip(trottleRange, t)
        Log.i("JOYSTICK", "throttle ${t}")
        cachedSpeed.postValue(t)
        lastForcingTrottleTime = LocalDateTime.now()
    }

    init{
        timeLooper = TimeLooper(GracePeriodDuration) {
            val now = LocalDateTime.now()

            if (!directionalAnchoring.isAtAnchor(cachedDirection.cachedValue!!.toFloat())) {
                val duration = Duration.between(lastForcingDirectionTime, now).abs()
                if (duration.toMillis() < GracePeriodDuration) {
                    return@TimeLooper
                }
                val newDirection =
                    directionalAnchoring.pullToAnchor(cachedDirection.cachedValue!!.toFloat())
                cachedDirection.postValue(newDirection)
                Log.i("JOYSTICK", "adjust Direction: $newDirection")
            }
            if (!throttleAnchoring.isAtAnchor(cachedSpeed.cachedValue!!.toFloat())) {
                val duration = Duration.between(lastForcingTrottleTime, now).abs()
                if (duration.toMillis() < GracePeriodDuration) {
                    return@TimeLooper
                }
                val newSpeed = throttleAnchoring.pullToAnchor(cachedSpeed.cachedValue!!.toFloat())
                cachedSpeed.postValue(newSpeed)
                Log.i("JOYSTICK", "adjust Speed: $newSpeed")
            }
        }
        timeLooper.ensureTimerStart()
    }




}