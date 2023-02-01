package com.example.racecarcontroller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MutableWithCache<T>(initValue: T) {

    private val mutableLiveData = MutableLiveData<T>()
    var cachedValue: T = initValue
        private set

    init {
        mutableLiveData.value = initValue!!
    }
    val liveData: LiveData<T>
        get() = mutableLiveData

    fun postValue(newVal: T){
        mutableLiveData.postValue(newVal!!)
        cachedValue = newVal
    }
    val savedValue: T
        get() = mutableLiveData.value!!
    
}