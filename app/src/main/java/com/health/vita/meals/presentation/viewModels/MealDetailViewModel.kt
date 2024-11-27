package com.health.vita.meals.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.health.vita.meals.data.repository.MealDetailRepository
import com.health.vita.meals.data.repository.MealDetailRepositoryImpl
import com.health.vita.meals.domain.model.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealDetailViewModel(
    private val mealDetailRepository : MealDetailRepository = MealDetailRepositoryImpl()
) : ViewModel() {

    fun toggleFavorite(isFavorite: Boolean): Unit {
        viewModelScope.launch(Dispatchers.IO){
            _meal.value?.let { mealDetailRepository.toggleFavorite(it,isFavorite) }
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
}