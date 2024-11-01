package com.health.vita.meals.presentation.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.error_management.CalculatingTrackingMealsDateRangeError
import com.health.vita.core.utils.error_management.ErrorManager
import com.health.vita.core.utils.error_management.NetworkError
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.repository.MealTrackingRepository
import com.health.vita.meals.data.repository.MealTrackingRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MealTrackingViewModel(

    private val mealTrackingRepository: MealTrackingRepository = MealTrackingRepositoryImpl()

) : ViewModel() {

    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _daysSinceRegisterNutritionalPlan = MutableLiveData(0)

    val daysSinceRegisterNutritionalPlan: LiveData<Int> get() = _daysSinceRegisterNutritionalPlan

    fun getRegisterPlanDate() {

        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }

            try {

                val daysRange = mealTrackingRepository.getTrackingDateRange()

                //TimeStamps not provided or user is not logged in

                if (daysRange == -1) {
                    Log.d("Meals tracking repository", "No nutritional plan creation date found.")
                    withContext(Dispatchers.Main) {
                        _uiHandler.setErrorState(CalculatingTrackingMealsDateRangeError())
                    }
                }

                Log.d("Meals tracking repository", "Range of day found $daysRange")

                withContext(Dispatchers.Main) {
                    _daysSinceRegisterNutritionalPlan.value = daysRange + 1
                    _uiHandler.setSuccess()
                }


            } catch (e: IOException) {

                ErrorManager.postError(NetworkError(cause = e))
                withContext(Dispatchers.Main) {
                    _uiHandler.setErrorState(NetworkError(cause = e))
                }
            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    _uiHandler.setErrorState(UnknownError(cause = e))
                }
            }

        }

    }
}