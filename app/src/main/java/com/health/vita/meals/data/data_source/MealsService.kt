package com.health.vita.meals.data.data_source

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface MealsService {
    suspend fun getMealsQuantity(userID: String): Int
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

}