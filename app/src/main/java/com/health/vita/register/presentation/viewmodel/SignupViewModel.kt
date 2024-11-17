package com.health.vita.register.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.auth.data.repository.AuthRepository
import com.health.vita.auth.data.repository.AuthRepositoryImpl
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
    private val profileImageRepository: ProfileImageRepository = ProfileImageRepositoryImpl(),
    private val authRepositoryImpl: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _name = MutableLiveData("")
    val name: LiveData<String> get() = _name

    private val _lastName = MutableLiveData("")
    val lastName: LiveData<String> get() = _lastName

    private val _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    //user profile image
    private var _profileImage = MutableLiveData<String?>()
    val profileImage: LiveData<String?> get() = _profileImage

    private val _isDefaultImage = MutableLiveData<Boolean>()
    val isDefaultImage: LiveData<Boolean> get() = _isDefaultImage

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

    fun selectDefaultImage(imageUrl: String?) {
        _profileImage.value = imageUrl
        _isDefaultImage.value = true
    }

    fun selectUploadedImage(uri: Uri?) {
        _profileImage.value = uri?.toString()
        _isDefaultImage.value = false
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
                    imageID = _profileImage.value,
                    weight = (_weight.value ?: 0f),
                    height = (_height.value ?: 0f),
                    physicalLevel = _activityLevel.value ?: 0,
                    sex = _gender.value ?: "",
                    physicalTarget = _goal.value ?: "",
                )


                Log.e("SIGN-UP VIEW MODEL", "entrando en el signup")

                signUpRepository.signup(user, _password.value ?: "")

                updateProfileImage()


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

    fun updateProfileImage() {

        val currentImage = _profileImage.value ?: return
        val isDefault = _isDefaultImage.value ?: true


        viewModelScope.launch(Dispatchers.IO) {

            try {

                var imageId = profileImageRepository.uploadUserProfileImage(Uri.parse(currentImage), isDefault)

                profileImageRepository.updateUserProfileImageID(imageId)

                withContext(Dispatchers.Main){
                    _profileImage.value =  imageId
                }


            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    Log.e("SIGN-UP VIEW MODEL", e.message ?: "Error al cargar la imagen de perfil")
                    uiHandler.setErrorState(UnknownError("Error al cargar la imagen de perfil", e))
                    ErrorManager.postError(NetworkError(cause = e))
                }
            }
        }
    }


}
