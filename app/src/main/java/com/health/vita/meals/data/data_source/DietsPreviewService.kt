package com.health.vita.meals.data.data_source

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.health.vita.meals.domain.model.Meal
import kotlinx.coroutines.tasks.await

interface DietsPreviewService {
    suspend fun generateMealsIA(userId: String, meals: List<Meal>): Boolean
    suspend fun getMealsIA(userId: String): List<Meal>
}

class DietsPreviewServiceImpl : DietsPreviewService {
    override suspend fun generateMealsIA(userId: String, meals: List<Meal>): Boolean {
        return try {
            val mealsIACollection = Firebase.firestore
                .collection("User")
                .document(userId)
                .collection("MealsIA")

            meals.forEach { meal ->
                val mealWithId = meal.copy(id = mealsIACollection.document().id)
                mealsIACollection.document(mealWithId.id).set(mealWithId).await()
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getMealsIA(userId: String): List<Meal> {
        return try {
            val mealsIACollection = Firebase.firestore
                .collection("User")
                .document(userId)
                .collection("MealsIA")
                .get()
                .await()

            mealsIACollection.documents.mapNotNull { document ->
                document.toObject(Meal::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
