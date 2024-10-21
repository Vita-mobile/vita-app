package com.health.vita.register.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.error_management.AuthCredentialsError
import com.health.vita.core.utils.error_management.DatabaseError
import com.health.vita.core.utils.error_management.ErrorManager
import com.health.vita.core.utils.error_management.NetworkError
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.domain.model.User
import com.health.vita.register.data.repository.SignUpRepository
import com.health.vita.register.data.repository.SignUpRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.sql.SQLException

class SignupViewModel(
    private val signUpRepository: SignUpRepository = SignUpRepositoryImpl()
) : ViewModel() {

    private val _name = MutableLiveData("")
    val name: LiveData<String> get() = _name

    private val _password = MutableLiveData("")
    val password: LiveData<String> get() = _password

    private val _email = MutableLiveData("")
    val email: LiveData<String> get() = _email

    private val _photoUri = MutableLiveData("")
    val photoUri: LiveData<String> get() = _photoUri

    private val _age = MutableLiveData(0)
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

    private val uiHandler = UiHandler()

    val uiState: LiveData<UiState> get() = uiHandler.uiState


    fun setPassword(password: String) {
        _password.value = password
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPhotoUri(uri: String) {
        _photoUri.value = uri
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

    fun registerOperation() {

        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                uiHandler.setLoadingState()
            }

            try {

                val user = User(
                    id = "",
                    name = _name.value ?: "",
                    email = _email.value ?: "",
                    //photoUri = _photoUri.value ?: "",
                    weight = (_weight.value ?: 0f),
                    age = _age.value ?: 0,
                    height = (_height.value ?: 0f),
                    gender = _gender.value ?: "",
                    goal = _goal.value ?: "",
                    activityLevel = _activityLevel.value ?: 0
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
