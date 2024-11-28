package com.health.vita.meals.presentation.viewModels

import android.content.Context
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
import com.health.vita.meals.data.repository.MealsRepository
import com.health.vita.meals.data.repository.MealsRepositoryImpl
import com.health.vita.meals.data.repository.NutritionalPlanRepository
import com.health.vita.meals.data.repository.NutritionalPlanRepositoryImpl
import com.health.vita.profile.data.repository.UserRepository
import com.health.vita.profile.data.repository.UserRepositoryImpl
import com.health.vita.register.data.repository.ProfileImageRepository
import com.health.vita.register.data.repository.ProfileImageRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Calendar

class MealsViewModel(
    private val mealsRepository: MealsRepository = MealsRepositoryImpl(),
    private val userRepository: UserRepository = UserRepositoryImpl(),
    private val nutritionalPlanRepository: NutritionalPlanRepository = NutritionalPlanRepositoryImpl(),
    private val imageRepository: ProfileImageRepository = ProfileImageRepositoryImpl()

) : ViewModel() {
    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _user = MutableLiveData<User?>(User())
    val user: LiveData<User?> get() = _user

    private val _kcal = MutableLiveData(0)
    val kcal: LiveData<Int> get() = _kcal
    private val _lastRecordedMeal = MutableLiveData<Int>()
    val lastRecordedMeal: LiveData<Int> get() = _lastRecordedMeal
    private val _mealCount = MutableLiveData<Int>()
    val mealCounts: LiveData<Int> get() = _mealCount
    private val _lastEatenMeal = MutableLiveData<Boolean>()

    private val _profileImageUrl = MutableLiveData<String?>()
    val profileImageUrl: LiveData<String?> get() = _profileImageUrl

    fun getProfileImage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.e("ProfileViewModel", "Getting profile image")
                val image = imageRepository.getProfileImage()
                withContext(Dispatchers.Main) {
                    _profileImageUrl.value = image
                }

            } catch (e: IOException) {

                withContext(Dispatchers.Main) {
                    ErrorManager.postError(NetworkError("Error con la red.", cause = e))
                    _uiHandler.setErrorState(NetworkError(cause = e))
                }
            } catch (e: FirebaseException) {
                withContext(Dispatchers.Main) {
                    ErrorManager.postError(FirebaseError("Error al momento de usar firebase.", e))
                    _uiHandler.setErrorState(FirebaseError(cause = e))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    ErrorManager.postError(UnknownError("Error desconocido.", e))
                    _uiHandler.setErrorState(UnknownError(cause = e))
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

    fun fetchMealsState() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
                getLastEatenMeal()
                getCurrentMeal()
                _mealCount.value = mealsRepository.getMealsCount()
                _uiHandler.setSuccess()
            }
        }
    }

    fun getCurrentMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _lastRecordedMeal.value = mealsRepository.getLastMealIndex()
            }
        }
    }


    fun getLastEatenMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            val date = mealsRepository.getLastEatenMealDate()
            val today = Calendar.getInstance()
            val lastEatenDate = Calendar.getInstance().apply {
                time = date.toDate()
            }
            val lastEaten = lastEatenDate.get(Calendar.YEAR) >= today.get(Calendar.YEAR) &&
                    lastEatenDate.get(Calendar.DAY_OF_YEAR) >= today.get(Calendar.DAY_OF_YEAR)
            withContext(Dispatchers.Main) {
                _lastEatenMeal.value = lastEaten;
            }
            if (_lastEatenMeal.value == false) {
                Log.e(">>>", "Fue reseteado")
                resetMealIndex()
            }
        }
    }

    private fun resetMealIndex() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                mealsRepository.resetMealIndex()
                _lastRecordedMeal.value = mealsRepository.getLastMealIndex()
            }

        }
    }

    fun obtainDailyCalories() {

        viewModelScope.launch(Dispatchers.IO) {

            val nutritionalPlan = nutritionalPlanRepository.getNutritionalPlan()

            withContext(Dispatchers.Main) {
                _kcal.value = nutritionalPlan?.kcalGoal?.toInt() ?: 0
            }

        }
    }
}
