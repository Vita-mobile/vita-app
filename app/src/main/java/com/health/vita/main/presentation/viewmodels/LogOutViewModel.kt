package com.health.vita.main.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.auth.data.repository.AuthRepository
import com.health.vita.auth.data.repository.AuthRepositoryImpl
import com.health.vita.core.utils.error_management.ErrorManager
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogOutViewModel(

    val authRepository: AuthRepository = AuthRepositoryImpl()

) : ViewModel() {


    private val uiHandler: UiHandler = UiHandler()

    val uiState: LiveData<UiState> get() = uiHandler.uiState

    fun Logout() {

        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                uiHandler.setIdleState()
            }

            try {

                authRepository.singout()
                uiHandler.setSuccess()

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {

                    val error = UnknownError(
                        cause = e,
                        message = "Error al tratar de cerrar sesión. Intenta de nuevo."
                    )
                    uiHandler.setErrorState(error)
                    ErrorManager.postError(error)
                }
            }
        }
    }
}