package com.health.vita.meals.data.data_source

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.health.vita.meals.domain.model.IngredientMeal
import kotlinx.coroutines.tasks.await

interface IngredientService {
    suspend fun getIngredients(): List<IngredientMeal?>
}

class IngredientServiceImpl():IngredientService {
    override suspend fun getIngredients(): List<IngredientMeal?> {
        val result = Firebase.firestore
            .collection("Ingredient")
            .get()
            .await()
        val ingredientMeals = result.documents.map { doc ->
            doc.toObject(IngredientMeal::class.java)
        }
        return ingredientMeals
    }
}