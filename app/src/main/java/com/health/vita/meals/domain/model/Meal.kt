package com.health.vita.meals.domain.model

data class Meal (

    var id: String = "",
    var name: String = "",
    var description: String = "",

    var calories: Float = 0.0f,
    var carbs: Float = 0.0f,
    var fats: Float = 0.0f,
    var proteins: Float = 0.0f,

    var ingredients: List<IngredientMeal> = emptyList(),
    var meal: Int = 0,

    )