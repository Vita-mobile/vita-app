package com.health.vita.meals.data.data_source

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.health.vita.meals.domain.model.Meal
import kotlinx.coroutines.tasks.await

interface MealDetailService {
    suspend fun removeFavMeal(mealID: String, userID: String): Unit
    suspend fun addFavMeal(meal: Meal, userId: String): Unit
}

class MealDetailServiceImp : MealDetailService {
    override suspend fun removeFavMeal(mealID: String, userID: String) {
        try {
            Firebase.firestore
                .collection("User")
                .document(userID)
                .collection("Favorites")
                .document(mealID)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(">>>", "Error removing meal from favorites: ${e.message}")
        }

    }

    override suspend fun addFavMeal(meal: Meal, userId: String) {
        try {
            Firebase.firestore
                .collection("User")
                .document(userId)
                .collection("Favorites")
                .document(meal.id)
                .set(meal)
                .await()
        } catch (e: Exception) {
            Log.e(">>>","Error adding meal to favorites: ${e.message}")
        }
    }
}