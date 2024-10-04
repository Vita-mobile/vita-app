package com.health.vita.core.utils.error_management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object ErrorManager {

    //Mutablelivedata is val due to we don't want to modify its reference.
    private val  _errorLiveData = MutableLiveData<EventOnceManager<AppError>>()
    val errorLiveData: LiveData<EventOnceManager<AppError>> get() = _errorLiveData

    fun postError(error :AppError){

        _errorLiveData.value = EventOnceManager(error)

    }
}