package com.health.vita.meals.domain.model

data class Ingredient (
    var calories: Int = 0,
    var carbs: Int = 0,
    var fats: Int = 0,
    var id: String = "",
    var name: String = "",
    var protein: Int = 0
)