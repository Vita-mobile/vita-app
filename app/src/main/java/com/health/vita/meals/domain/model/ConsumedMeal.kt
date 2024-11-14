package com.health.vita.meals.domain.model

import com.google.firebase.Timestamp

data class ConsumedMeal (
    var id: String = "",
    var name: String = "",
    var description: String = "",

    var calories: Float = 0.0f,
    var carbs: Float = 0.0f,
    var fats: Float = 0.0f,
    var proteins: Float = 0.0f,

    var ingredients: List<Ingredient> = emptyList(),
    var meal: Int = 0,
    var consumeDate: Timestamp = Timestamp.now()
    )

fun Meal.toConsumedMeal(): ConsumedMeal {
    return ConsumedMeal(
        id = "",
        name = this.name,
        description = this.description,
        calories = this.calories,
        carbs = this.carbs,
        fats = this.fats,
        proteins = this.proteins,
        ingredients = this.ingredients,
        meal = this.meal,
        consumeDate = Timestamp.now()
    )
}