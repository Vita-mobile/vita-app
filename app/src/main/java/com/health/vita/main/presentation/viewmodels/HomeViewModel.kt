package com.health.vita.main.presentation.viewmodels

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

class HomeViewModel(
    val userRepository: UserRepository = UserRepositoryImpl(),
    val imageRepository: ProfileImageRepository = ProfileImageRepositoryImpl()
) : ViewModel()  {

    private val _profileImageUrl = MutableLiveData<String?>()
    val profileImageUrl: LiveData<String?> get() = _profileImageUrl

    private val _user = MutableLiveData<User?>(User())
    val user: LiveData<User?> get() = _user

    private val _weight = MutableLiveData(0f)
    val weight: LiveData<Float> get() = _weight

    private val _height = MutableLiveData(0f)
    val height: LiveData<Float> get() = _height

    private val uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = uiHandler.uiState

    fun setWeight(weight: Float) {
        _weight.value = weight
    }

    fun setHeight(height: Float) {
        _height.value = height
    }

    fun getProfileImage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                withContext(Dispatchers.Main) {
                    uiHandler.setLoadingState()
                }
                Log.e("ProfileViewModel", "Getting profile image")
                val image = imageRepository.getProfileImage()
                withContext(Dispatchers.Main) {


                    _profileImageUrl.value = image
                    uiHandler.setSuccess()
                }

            } catch (e: IOException) {

                withContext(Dispatchers.Main) {
                    ErrorManager.postError(NetworkError("Error con la red.",cause = e))
                    uiHandler.setErrorState(NetworkError(cause = e))
                }
            } catch (e: FirebaseException) {
                withContext(Dispatchers.Main) {
                    ErrorManager.postError(FirebaseError("Error al momento de usar firebase.", e))
                    uiHandler.setErrorState(FirebaseError(cause = e))
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", e.stackTraceToString())
                withContext(Dispatchers.Main) {
                    ErrorManager.postError(UnknownError("Error desconocido en getProfileImage.", e))
                    uiHandler.setErrorState(UnknownError(cause = e))
                }
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                uiHandler.setLoadingState()
            }


            try {
                val me = userRepository.getCurrentUser()
                withContext(Dispatchers.Main) {
                    Log.e("ProfileViewModel", "Seteando el usuario$me")
                    _user.value = me
                    _weight.value = me?.weight ?: 0f
                    _height.value = me?.height ?: 0f

                    uiHandler.setSuccess()
                }

            } catch (e: Exception) {

                Log.e("ProfileViewModel", e.message ?: "Error desconocido en getCurrentUser")
                Log.e("ProfileViewModel", e.stackTraceToString())
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(
                        UnknownError(
                            "Error al actualizar datos del usuario",
                            e
                        )
                    )
                }
            }

        }
    }
}