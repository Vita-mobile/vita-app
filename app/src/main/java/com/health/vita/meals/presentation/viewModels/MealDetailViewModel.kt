package com.health.vita.meals.presentation.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.health.vita.core.utils.error_management.DatabaseError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.repository.DietsPreviewRepository
import com.health.vita.meals.data.repository.DietsPreviewRepositoryImpl
import com.health.vita.meals.data.repository.MealDetailRepository
import com.health.vita.meals.data.repository.MealDetailRepositoryImpl
import com.health.vita.meals.data.repository.MealsRepository
import com.health.vita.meals.data.repository.MealsRepositoryImpl
import com.health.vita.meals.domain.model.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealDetailViewModel(
    contex: Context,
    private val mealDetailRepository: MealDetailRepository = MealDetailRepositoryImpl(),
    private val mealPreviewRepository: DietsPreviewRepository = DietsPreviewRepositoryImpl(),
    private val mealsRepository: MealsRepository = MealsRepositoryImpl(contex)
) : ViewModel() {

    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _consumeMealState = MutableLiveData<Boolean>()
    val consumeMealState: LiveData<Boolean> = _consumeMealState

    fun toggleFavorite(isFavorite: Boolean): Unit {
        viewModelScope.launch(Dispatchers.IO) {
            _meal.value?.let { mealDetailRepository.toggleFavorite(it, isFavorite) }
        }
    }

    private val _meal = MutableLiveData<Meal>()
    val meal: LiveData<Meal> get() = _meal

    private val gson = Gson()

    fun setMealFromJson(mealJson: String) {
        val mealObj: Meal = gson.fromJson(mealJson, Meal::class.java)
        _meal.value = mealObj
        calculateMacroDominant(mealObj)
        calculateWeight(mealObj)
    }

    private val _pesoTotal = MutableLiveData(0)
    val pesoTotal: LiveData<Int> get() = _pesoTotal

    private val _macroDominantImage = MutableLiveData<Int>()
    val macroDominantImage: LiveData<Int> get() = _macroDominantImage

    private fun calculateMacroDominant(meal: Meal) {
        val protein = meal.proteins
        val carbs = meal.carbs
        val fats = meal.fats

        val dominantImage = when {
            protein > carbs && protein > fats -> com.health.vita.R.drawable.proteina
            carbs > protein && carbs > fats -> com.health.vita.R.drawable.carbohidrato
            else -> com.health.vita.R.drawable.grasas
        }
        _macroDominantImage.value = dominantImage

    }

    private fun calculateWeight(meal: Meal) {
        _pesoTotal.value = meal.ingredientMeals.sumOf { it.grams.toInt() }
    }

    fun consumeMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            _meal.value?.let {
                try{
                val isConsumed = mealPreviewRepository.consumeMeal(it)
                    if (isConsumed) {
                        withContext(Dispatchers.Main) {
                            mealsRepository.incrementMealIndex()
                            _uiHandler.setSuccess()
                            _consumeMealState.value = true
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            _uiHandler.setErrorState(DatabaseError())
                            _consumeMealState.value = false
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        _uiHandler.setErrorState(DatabaseError())
                        _consumeMealState.value = false
                    }
                }
            }
        }
    }
}
