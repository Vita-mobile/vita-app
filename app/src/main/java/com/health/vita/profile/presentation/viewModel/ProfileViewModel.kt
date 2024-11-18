package com.health.vita.profile.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.health.vita.core.utils.error_management.ErrorManager
import com.health.vita.core.utils.error_management.FirebaseError
import com.health.vita.core.utils.error_management.NetworkError
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.domain.model.User
import com.health.vita.profile.data.repository.UserRepository
import com.health.vita.profile.data.repository.UserRepositoryImpl
import com.health.vita.register.data.repository.ProfileImageRepository
import com.health.vita.register.data.repository.ProfileImageRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ProfileViewModel(val userRepository: UserRepository = UserRepositoryImpl(),


    val imageRepository: ProfileImageRepository = ProfileImageRepositoryImpl()

): ViewModel() {


    private val _profileImageUrl = MutableLiveData<String?>()
    val profileImageUrl: LiveData<String?> get() = _profileImageUrl

    private val _user = MutableLiveData<User?>(User())
    val user: LiveData<User?> get() = _user

    private val uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = uiHandler.uiState



    fun getProfileImage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                Log.e("ProfileViewModel", "Getting profile image")
                val image = imageRepository.getProfileImage()
                withContext(Dispatchers.Main) {
                    _profileImageUrl.value = image
                }

            } catch (e: IOException) {

                ErrorManager.postError(NetworkError("Error con la red.",cause = e))
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(NetworkError(cause = e))
                }
            } catch (e: FirebaseException) {
                ErrorManager.postError(FirebaseError("Error al momento de usar firebase.", e))
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(FirebaseError(cause = e))
                }
            } catch (e: Exception) {
                ErrorManager.postError(UnknownError("Error desconocido.", e))
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(UnknownError(cause = e))
                }
            }
        }
    }

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