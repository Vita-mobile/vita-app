package com.health.vita.auth.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.health.vita.auth.data.repository.AuthRepositoryImpl
import com.health.vita.core.utils.error_management.AuthCredentialsError
import com.health.vita.core.utils.error_management.FirebaseError
import com.health.vita.core.utils.error_management.ErrorManager
import com.health.vita.core.utils.error_management.NetworkError
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class LoginViewModel(

    val authRepositoryImpl: com.health.vita.auth.data.repository.AuthRepository = AuthRepositoryImpl()

) : ViewModel() {


    private val uiHandler = UiHandler()

    val uiStaate: LiveData<UiState> get() = uiHandler.uiState

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) { uiHandler.setLoadingState() }

            try {

                authRepositoryImpl.signin(email, password)

                withContext(Dispatchers.Main) { uiHandler.setSuccess() }

                Log.d("Login", "Despues del success")

            } catch (e: FirebaseAuthInvalidCredentialsException) {

                Log.d("Login", "Error al momento de autenticar.")

                withContext(Dispatchers.Main) {

                    uiHandler.setErrorState(AuthCredentialsError(cause = e))
                }


            } catch (e: IOException) {

                ErrorManager.postError(NetworkError(cause = e))
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(NetworkError(cause = e))
                }
            } catch (e: FirebaseException) {
                ErrorManager.postError(FirebaseError("Error al momento de autenticar", e))
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(FirebaseError(cause = e))
                }
            } catch (e: Exception) {
                ErrorManager.postError(UnknownError("Error desconocido", e))
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(UnknownError(cause = e))
                }
            }


        }


    }


}