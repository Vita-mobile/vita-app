package com.health.vita.meals.data.data_source

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface MealsService {
    suspend fun getMealsQuantity(userID: String): Int
    suspend fun resetMealIndex(userID: String): Unit;
    suspend fun incrementMealIndex(userID: String)
    suspend fun getLastMealIndex(userID: String): Int
}

class MealsServiceImpl : MealsService {
    override suspend fun getMealsQuantity(userID: String): Int {


        val nutritionalPlanSnapshot = Firebase.firestore.collection("User").document(userID).collection("NutritionalPlan").get().await()

        if (!nutritionalPlanSnapshot.isEmpty) {
            val doc = nutritionalPlanSnapshot.documents.first()
            val meals = doc.get("meals").toString().toInt()
            return meals
        }
        return 0
    }

    override suspend fun resetMealIndex(userID: String) {
        try {
            val snapshot = Firebase.firestore
                .collection("User")
                .document(userID)
                .collection("NutritionalPlan")
                .limit(1)
                .get()
                .await()

            val documentId = snapshot.documents.firstOrNull()?.id
            if (documentId != null) {
                Firebase.firestore
                    .collection("User")
                    .document(userID)
                    .collection("NutritionalPlan")
                    .document(documentId)
                    .update("mealsCount", 0)
                    .await()
            }
        } catch (e: Exception) {
            Log.e(">>>", "Error resetting meal count: ${e.message}")
        }
    }

    override suspend fun incrementMealIndex(userID: String) {
        try {
            val snapshot = Firebase.firestore
                .collection("User")
                .document(userID)
                .collection("NutritionalPlan")
                .limit(1)
                .get()
                .await()

            val documentId = snapshot.documents.firstOrNull()?.id
            if (documentId != null) {
                val doc = snapshot.documents.first()
                val currentMealsCount = doc.getLong("mealsCount") ?: 0L
                val newMealsCount = currentMealsCount + 1
                Firebase.firestore
                    .collection("User")
                    .document(userID)
                    .collection("NutritionalPlan")
                    .document(documentId)
                    .update("mealsCount", newMealsCount)
                    .await()
            }
        } catch (e: Exception) {
            Log.e(">>>", "Error incrementing meal count: ${e.message}")
        }
    }

    override suspend fun getLastMealIndex(userID: String): Int {
        return try {
            val snapshot = Firebase.firestore
                .collection("User")
                .document(userID)
                .collection("NutritionalPlan")
                .limit(1)
                .get()
                .await()

            val document = snapshot.documents.firstOrNull()
            if (document != null) {
                val mealsCount = document.getLong("mealsCount")?.toInt() ?: 0
                mealsCount
            } else {
                0
            }
        } catch (e: Exception) {
            Log.e(">>>", "Error getting last meal count: ${e.message}")
            0
        }
    }



}