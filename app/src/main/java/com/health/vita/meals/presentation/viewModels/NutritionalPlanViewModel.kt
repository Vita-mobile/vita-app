package com.health.vita.meals.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.error_management.NetworkError
import com.health.vita.core.utils.error_management.UnknownError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.repository.IngredientRepository
import com.health.vita.meals.data.repository.IngredientRepositoryImpl
import com.health.vita.meals.data.repository.NutritionalPlanRepository
import com.health.vita.meals.data.repository.NutritionalPlanRepositoryImpl
import com.health.vita.meals.domain.model.IngredientMeal
import com.health.vita.meals.domain.model.NutritionalPlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class NutritionalPlanViewModel(

    private val nutritionalPlanRepository: NutritionalPlanRepository = NutritionalPlanRepositoryImpl(),
    private val ingredientRepository: IngredientRepository = IngredientRepositoryImpl()


) : ViewModel() {
    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _meals = MutableLiveData(3)
    val meals: LiveData<Int> get() = _meals

    private val _preferences = MutableLiveData<List<IngredientMeal>>(emptyList())
    val preferences: LiveData<List<IngredientMeal>> get() = _preferences
    private val _restrictions = MutableLiveData<List<IngredientMeal>>(emptyList())
    val restrictions: LiveData<List<IngredientMeal>> get() = _restrictions

    private val _ingredientsState = MutableLiveData<List<IngredientMeal?>>()
    val ingredientsState: LiveData<List<IngredientMeal?>> get() = _ingredientsState

    private val _nutritionalPlanState = MutableLiveData<NutritionalPlan?>()
    val nutritionalPlanState: LiveData<NutritionalPlan?> get() = _nutritionalPlanState

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    fun getIngredients() {
        viewModelScope.launch(Dispatchers.IO) {
            val ingredients = ingredientRepository.getIngredients()

            val ingredientMeals = ingredients.map { ingredient ->
                ingredient?.let {
                    IngredientMeal(
                        name = it.name,
                        id = it.id
                    )
                }
            }

            withContext(Dispatchers.Main) { _ingredientsState.value = ingredientMeals }
        }
    }

    fun getNutritionalPlan(){
        viewModelScope.launch(Dispatchers.IO) {
            val nutritionalPlan = nutritionalPlanRepository.getNutritionalPlan()
            withContext(Dispatchers.Main) {
                _nutritionalPlanState.value = nutritionalPlan
                nutritionalPlan?.let{
                    _preferences.value = it.preferences
                    _restrictions.value = it.restrictions
                    _meals.value = it.meals
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addPreference(ingredientMealToAdd: IngredientMeal) {
        val currentPreferences = _preferences.value ?: listOf()
        val updatedPreferences = currentPreferences + ingredientMealToAdd
        _preferences.value = updatedPreferences
    }

    fun addRestriction(ingredientMealToAdd: IngredientMeal) {
        val currentRestrictions = _restrictions.value ?: listOf()
        val updatedRestrictions = currentRestrictions + ingredientMealToAdd
        _restrictions.value = updatedRestrictions
    }

    fun deletePreference(ingredientMealToDelete: IngredientMeal) {
        val currentPreferences = _preferences.value ?: listOf()
        val updatedPreferences = currentPreferences.filter { it != ingredientMealToDelete }
        _preferences.value = updatedPreferences
    }

    fun deleteRestriction(ingredientMealToDelete: IngredientMeal) {
        val currentRestrictions = _restrictions.value ?: listOf()
        val updatedRestrictions = currentRestrictions.filter { it != ingredientMealToDelete }
        _restrictions.value = updatedRestrictions
    }


    fun setMeals(amount: Int) {
        _meals.value = amount
    }

    fun createNutritionalPlan() {
        viewModelScope.launch(Dispatchers.IO) {
            val nutritionalPlan = nutritionalPlanRepository.getNutritionalPlan()

            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }



            val preferences = _preferences.value ?: emptyList()
            val restrictions = _restrictions.value ?: emptyList()
            val meals = _meals.value ?: 3
            try {
                nutritionalPlan?.let{
                    val updates = mapOf(
                        "preferences" to (preferences),
                        "restrictions" to (restrictions),
                        "meals" to (meals)
                    )
                    nutritionalPlanRepository.updateNutritionalPlan(updates)
                } ?: run {
                    nutritionalPlanRepository.createNutritionalPlan(
                        preferences,
                        restrictions,
                        meals
                    )
                }

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