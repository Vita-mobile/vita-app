package com.health.vita.meals.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.repository.NutritionalPlanRepository
import com.health.vita.meals.data.repository.NutritionalPlanRepositoryImpl
import com.health.vita.meals.domain.model.Ingredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NutritionalPlanViewModel(

    private val nutritionalPlanRepository: NutritionalPlanRepository = NutritionalPlanRepositoryImpl()

) : ViewModel()  {
    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _meals = MutableLiveData(0)
    val meals: LiveData<Int> get() = _meals

    private val _preferences = MutableLiveData<List<Ingredient>>(emptyList())
    val preferences : LiveData<List<Ingredient>> get() = _preferences
    private val _restrictions = MutableLiveData<List<Ingredient>>(emptyList())
    val restrictions : LiveData<List<Ingredient>> get() = _restrictions

    fun addPreference(ingredientToAdd: Ingredient) {
        val currentPreferences = _preferences.value ?: listOf()
        val updatedPreferences = currentPreferences + ingredientToAdd
        _preferences.value = updatedPreferences
    }
    fun addRestriction(ingredientToAdd: Ingredient) {
        val currentRestrictions = _restrictions.value ?: listOf()
        val updatedRestrictions = currentRestrictions + ingredientToAdd
        _restrictions.value = updatedRestrictions
    }

    fun createNutritionalPlan(){
        viewModelScope.launch(Dispatchers.IO) {
            val preferences = _preferences.value ?: emptyList()
            val restrictions = _preferences.value ?: emptyList()
            val meals  = _meals.value ?: 0
            nutritionalPlanRepository.createNutritionalPlan(preferences, restrictions, meals)
        }

    }


}