package com.health.vita.meals.data.data_source

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.health.vita.meals.domain.model.NutritionalPlan
import kotlinx.coroutines.tasks.await

interface NutritionalPlanService {
    suspend fun createNutritionalPlan(nutritionalPlan: NutritionalPlan, userId: String): Boolean
}

class NutritionalPlanServiceImpl() : NutritionalPlanService{
    override suspend fun createNutritionalPlan(nutritionalPlan: NutritionalPlan, userId: String): Boolean {

        try {
            Firebase.firestore.collection("User")
                .document(userId)
                .collection("NutritionalPlan")
                .document(nutritionalPlan.id)
                .set(nutritionalPlan)
                .await()
            return true
        } catch (e: Exception){
            Log.e(">>>", "Error durante la creacion: ${e.message}")
            return false
        }

    }

}