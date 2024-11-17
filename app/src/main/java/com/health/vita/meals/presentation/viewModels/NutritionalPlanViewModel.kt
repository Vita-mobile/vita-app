package com.health.vita.meals.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.error_management.NetworkError
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.repository.NutritionalPlanRepository
import com.health.vita.meals.data.repository.NutritionalPlanRepositoryImpl
import com.health.vita.meals.domain.model.IngredientMeal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class NutritionalPlanViewModel(

    private val nutritionalPlanRepository: NutritionalPlanRepository = NutritionalPlanRepositoryImpl()

) : ViewModel() {
    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _meals = MutableLiveData(3)
    val meals: LiveData<Int> get() = _meals

    private val _preferences = MutableLiveData<List<IngredientMeal>>(emptyList())
    val preferences: LiveData<List<IngredientMeal>> get() = _preferences
    private val _restrictions = MutableLiveData<List<IngredientMeal>>(emptyList())
    val restrictions: LiveData<List<IngredientMeal>> get() = _restrictions

    fun addPreference(ingredientToAdd: IngredientMeal) {
        val currentPreferences = _preferences.value ?: listOf()
        val updatedPreferences = currentPreferences + ingredientToAdd
        _preferences.value = updatedPreferences
    }

    fun addRestriction(ingredientToAdd: IngredientMeal) {
        val currentRestrictions = _restrictions.value ?: listOf()
        val updatedRestrictions = currentRestrictions + ingredientToAdd
        _restrictions.value = updatedRestrictions
    }

    fun setMeals(amount: Int) {
        _meals.value = amount
    }

    fun createNutritionalPlan() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }
            val preferences = _preferences.value ?: emptyList()
            val restrictions = _restrictions.value ?: emptyList()
            val meals = _meals.value ?: 3
            try {
                nutritionalPlanRepository.createNutritionalPlan(
                    preferences,
                    restrictions,
                    meals
                )
                withContext(Dispatchers.Main) {
                    _uiHandler.setSuccess()
                }

            } catch (e: IOException) {

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