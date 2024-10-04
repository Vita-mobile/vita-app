package com.health.vita.core.utils.states_management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.health.vita.core.utils.error_management.AppError

// UiState.kt
sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    object Idle : UiState()
    data class Error(val error: AppError) : UiState()
}

class UiHandler(initialState: UiState = UiState.Idle) {

    private val _uiState = MutableLiveData<UiState>(initialState)
    val uiState: LiveData<UiState> get() = _uiState

    fun setErrorState(error: AppError) {
        _uiState.value = UiState.Error(error)
    }

    fun setLoadingState() {
        _uiState.value = UiState.Loading
    }

    fun setIdleState() {

        _uiState.value = UiState.Idle
    }

    fun setSuccess() {

        _uiState.value = UiState.Success
    }

}