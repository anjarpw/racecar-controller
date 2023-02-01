package com.example.racecarcontroller

import android.util.Log
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

class TimeLooper(duration: Long, runner: () -> Unit) {
    private var timer: Timer? = null

    val runner = runner
    val duration = duration

    fun ensureTimerStart() {
        if (timer == null) {
            startTimer()
        }
    }

    private fun startTimer() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runner()
            }
        }, 0, duration)
    }
}