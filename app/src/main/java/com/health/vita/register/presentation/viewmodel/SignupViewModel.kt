package com.health.vita.register.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.error_management.DatabaseError
import com.health.vita.core.utils.error_management.ErrorManager
import com.health.vita.core.utils.error_management.NetworkError
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.domain.model.User
import com.health.vita.register.data.repository.ProfileImageRepository
import com.health.vita.register.data.repository.ProfileImageRepositoryImpl
import com.health.vita.register.data.repository.SignUpRepository
import com.health.vita.register.data.repository.SignUpRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.sql.SQLException
import java.util.UUID

class SignupViewModel(
    private val signUpRepository: SignUpRepository = SignUpRepositoryImpl(),
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepositoryImpl()
) : ViewModel() {

    private val _name = MutableLiveData("")
    val name: LiveData<String> get() = _name

    private val _lastName = MutableLiveData("")
    val lastName: LiveData<String> get() = _lastName

    private val _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private val _profileImage = MutableLiveData<String?>()
    val profileImage: LiveData<String?> get() = _profileImage

    private val _age = MutableLiveData(18)
    val age: LiveData<Int> get() = _age

    private val _weight = MutableLiveData(0f)
    val weight: LiveData<Float> get() = _weight

    private val _height = MutableLiveData(0f)
    val height: LiveData<Float> get() = _height

    private val _gender = MutableLiveData("")
    val gender: LiveData<String> get() = _gender

    private val _activityLevel = MutableLiveData(0)
    val activityLevel: LiveData<Int> get() = _activityLevel

    private val _goal = MutableLiveData("")
    val goal: LiveData<String> get() = _goal


    private val _defaultImages = MutableLiveData<List<String>>()
    val defaultImages: LiveData<List<String>> get() = _defaultImages

    private val _isProfileImageLoading = MutableLiveData<Boolean>(false)
    val isProfileImageLoading: LiveData<Boolean> get() = _isProfileImageLoading

    private val uiHandler = UiHandler()

    val uiState: LiveData<UiState> get() = uiHandler.uiState

    private val _isEmailRepeated = MutableLiveData<Boolean>()

    val isEmailRepeated: LiveData<Boolean> get() = _isEmailRepeated


    fun setPassword(password: String) {
        _password.value = password
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setLastName(lastName: String) {
        _lastName.value = lastName
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setProfileImage(uri: String?) {
        _profileImage.value = uri
    }

    fun setIsProfileImageLoading(boolean: Boolean) {
        _isProfileImageLoading.value = boolean
    }

    fun setActivityLevel(activityLevel: Int) {
        _activityLevel.value = activityLevel
    }

    fun setWeight(weight: Float) {
        _weight.value = weight
    }

    fun setHeight(height: Float) {
        _height.value = height
    }

    fun setGoal(goal: String) {
        _goal.value = goal
    }

    fun setAge(age: Int) {
        _age.value = age
    }

    fun setGender(gender: String) {
        _gender.value = gender
    }

    fun loadDefaultImages() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val images = profileImageRepository.getDefaultProfileImages()

                withContext(Dispatchers.Main) {
                    _defaultImages.value = images
                }

            } catch (e: SQLException) {

                withContext(Dispatchers.Main) {
                    Log.e("SIGN-UP VIEW  MODEL", e.message ?: "Error desconocido")
                    uiHandler.setErrorState(DatabaseError("Error en la base de datos", e))
                    ErrorManager.postError(NetworkError(cause = e))
                }

            }
        }

    }

    fun updateProfileImage(uri: Uri?, isDefault: Boolean) {
        _isProfileImageLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {

            try {

                profileImageRepository.uploadUserProfileImage(uri, isDefault)

                withContext(Dispatchers.Main){
                    _profileImage.value = if (isDefault) {
                    uri?.lastPathSegment
                } else  {
                    uri?.toString()
                }
                    _isProfileImageLoading.value = false
                }


            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    _isProfileImageLoading.value = false
                    Log.e("SIGN-UP VIEW MODEL", e.message ?: "Error al cargar la imagen de perfil")
                    uiHandler.setErrorState(UnknownError("Error al cargar la imagen de perfil", e))
                    ErrorManager.postError(NetworkError(cause = e))
                }
            }
        }
    }

    fun isRepeatedEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isRepeated = signUpRepository.isRepeatedEmail(email ?: "")
                withContext(Dispatchers.Main) {
                    _isEmailRepeated.value = isRepeated
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(UnknownError("Error desconocido", e))
                    Log.e("SIGN-UP VIEW MODEL", e.message ?: "Error desconocido")
                    ErrorManager.postError(NetworkError(cause = e))
                }
            }
        }
    }

    fun registerOperation() {

        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                uiHandler.setLoadingState()
            }

            try {

                val user = User(
                    id = "",
                    name = _name.value ?: "",
                    lastName = _lastName.value ?: "",
                    email = _email.value ?: "",
                    age = _age.value ?: 0,
                    imageID = _profileImage.value?: null,
                    weight = (_weight.value ?: 0f),
                    height = (_height.value ?: 0f),
                    physicalLevel = _activityLevel.value ?: 0,
                    sex = _gender.value ?: "",
                    physicalTarget = _goal.value ?: "",
                )

                signUpRepository.signup(user, _password.value ?: "")

                withContext(Dispatchers.Main) {
                    uiHandler.setSuccess()
                }

            } catch (e: IOException) {

                withContext(Dispatchers.Main) {
                    Log.e("SIGN-UP VIEW  MODEL", e.message ?: "Error desconocido")
                    uiHandler.setErrorState(NetworkError("Fallo de conexión", e))
                    ErrorManager.postError(NetworkError(cause = e))
                }


            } catch (e: SQLException) {

                withContext(Dispatchers.Main) {
                    Log.e("SIGN-UP VIEW  MODEL", e.message ?: "Error desconocido")
                    uiHandler.setErrorState(DatabaseError("Error en la base de datos", e))
                    ErrorManager.postError(NetworkError(cause = e))
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    uiHandler.setErrorState(UnknownError("Error desconocido", e))
                    Log.e("SIGN-UP VIEW  MODEL", e.message ?: "Error desconocido")
                    ErrorManager.postError(NetworkError(cause = e))
                }

            }
        }
    }

}
