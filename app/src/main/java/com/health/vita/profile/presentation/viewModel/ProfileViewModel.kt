package com.health.vita.profile.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.domain.model.User
import com.health.vita.profile.data.repository.UserRepository
import com.health.vita.profile.data.repository.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(val userRepository: UserRepository = UserRepositoryImpl()): ViewModel() {


    private val _user = MutableLiveData<User?>(User())
    val user: LiveData<User?> get() = _user

    private val uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = uiHandler.uiState

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = userRepository.getCurrentUser()
            withContext(Dispatchers.Main) {
                _user.value = me
            }
        }
    }

    fun updatePersonalUserData(user: User){

        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                uiHandler.setLoadingState()
            }

            try {

                val updatedUser = userRepository.updateUserData(user)

                withContext(Dispatchers.Main) {
                    _user.value = updatedUser
                    uiHandler.setSuccess()
                }

            }  catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(UnknownError("Error al actualizar datos del usuario", e))
                }
            }


        }
    }
}