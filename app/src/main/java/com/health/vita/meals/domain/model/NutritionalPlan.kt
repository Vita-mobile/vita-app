package com.health.vita.meals.domain.model

import com.google.firebase.Timestamp

data class NutritionalPlan (

    var id: String = "",
    var carbs: Float = 0f,
    var fats: Float = 0f,
    var proteins: Float = 0f,
    var hydrationLevel: Float = 0f,
    var kcalGoal: Float = 0f,
    var meals: Int = 0,
    var registerPlanDate: Timestamp = Timestamp.now(),
    var preferences: List<IngredientMeal> = emptyList(),
    var restrictions: List<IngredientMeal> = emptyList(),

    )