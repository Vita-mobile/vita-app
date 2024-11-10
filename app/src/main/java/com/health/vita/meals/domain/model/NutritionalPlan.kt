package com.health.vita.meals.domain.model

import java.time.LocalDate

data class NutritionalPlan (

    var id: String = "",
    var carbs: Float = 0f,
    var fats: Float = 0f,
    var proteins: Float = 0f,
    var hydrationLevel: Float = 0f,
    var kcalGoal: Float = 0f,
    var meals: Int = 0,
    var registerPlanDate: LocalDate = LocalDate.now(),
    var preferences: List<Ingredient> = emptyList(),
    var restrictions: List<Ingredient> = emptyList(),

)