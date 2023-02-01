package com.example.racecarcontroller

import android.util.Log
import java.text.FieldPosition

open class Puller(feedbackSpeed: Float, tolerance: Float){
    var feedbackSpeed = feedbackSpeed
        protected set
    var tolerance = tolerance
        protected set

    fun isAtSomewhere(p: Float, somewhere: Float): Boolean {
        return Math.abs(p - somewhere) < tolerance
    }

    fun pullToSomewhere(newPosition: Float, somewhere: Float): Float {
        var p =  newPosition
        val diff = (somewhere - p) * feedbackSpeed
        p += diff
        if (isAtSomewhere(p, somewhere)) {
            p = somewhere
        }
        Log.i("JOYSTICK", "${newPosition} - ${p}, ${somewhere}, ${diff}")
        return p
    }

}

class Anchoring(anchor: Float, feedbackSpeed: Float, tolerance: Float): Puller(feedbackSpeed, tolerance) {

    var anchor = anchor
    fun update(anchor: Float, feedbackSpeed: Float, tolerance: Float) {
        this.anchor = anchor
        this.feedbackSpeed = feedbackSpeed
        this.tolerance = tolerance
    }
    fun isAtAnchor(p: Float): Boolean {
        return isAtSomewhere(p, anchor)
    }

    fun pullToAnchor(newPosition: Float): Float {
        return pullToSomewhere(newPosition, anchor)
    }


}

