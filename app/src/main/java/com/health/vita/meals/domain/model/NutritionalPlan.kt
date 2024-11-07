package com.health.vita.meals.domain.model

import java.time.LocalDate

data class NutritionalPlan (

    var id: String = "",
    var carbs: Float = 0f,
    var fats: Float = 0f,
    var proteins: Float = 0f,
    var hydrationLevel: Float = 0f,
    var kcalGoal: Float = 0f,
    var meals: Float = 0f,
    var registerPlanDate: LocalDate = LocalDate.now()

)