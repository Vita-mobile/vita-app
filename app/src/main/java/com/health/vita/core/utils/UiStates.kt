package com.health.vita.core.utils

import com.health.vita.core.utils.error_management.AppError

// UiState.kt
sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    object idle : UiState()
    data class Error(val error: AppError) : UiState()
}
