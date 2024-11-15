package com.health.vita.meals.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.health.vita.meals.domain.model.Meal

class MealDetailViewModel() : ViewModel() {

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
        val fats = meal.carbs

        val dominantImage = when {
            protein > carbs && protein > fats -> com.health.vita.R.drawable.proteina
            carbs > protein && carbs > fats -> com.health.vita.R.drawable.carbohidrato
            else -> com.health.vita.R.drawable.grasas
        }
        _macroDominantImage.value = dominantImage

    }

    private fun calculateWeight(meal: Meal) {
        _pesoTotal.value = meal.ingredients.sumOf { it.grams.toInt() }
    }
}