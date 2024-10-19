package com.health.vita.auth.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.health.vita.core.utils.error_management.AppError
import com.health.vita.core.utils.error_management.AuthError
import com.health.vita.core.utils.error_management.ErrorManager
import com.health.vita.core.utils.error_management.NetworkError
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class LoginViewModel : ViewModel() {


    private val uiHandler = UiHandler()

    val uiStaate: LiveData<UiState> get() = uiHandler.uiState

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) { uiHandler.setLoadingState() }

            try {

                // Here we should call the repository to login the user

                withContext(Dispatchers.Main) { uiHandler.setSuccess() }

            } catch (e: IOException) {

                ErrorManager.postError(NetworkError("Fallo de conexión", e))
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(
                        NetworkError(
                            "Fallo de conexión",
                            e
                        )
                    )
                }
            } catch (e: FirebaseAuthException) {
                ErrorManager.postError(AuthError("Error al momento de autenticar", e))
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(
                        AuthError(
                            "Error al momento de autenticar",
                            e
                        )
                    )
                }
            } catch (e: Exception) {
                ErrorManager.postError(UnknownError("Error desconocido", e))
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(
                        UnknownError(
                            "Error desconocido",
                            e
                        )
                    )
                }
            }


        }


    }


}