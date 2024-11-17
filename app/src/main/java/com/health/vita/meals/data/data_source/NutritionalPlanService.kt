package com.health.vita.meals.data.data_source

import android.util.Log
import androidx.compose.runtime.snapshots.Snapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.health.vita.domain.model.User
import com.health.vita.meals.domain.model.NutritionalPlan
import kotlinx.coroutines.tasks.await

interface NutritionalPlanService {
    suspend fun createNutritionalPlan(nutritionalPlan: NutritionalPlan, userId: String): Boolean
    suspend fun getNutritionalPlan(userId: String): NutritionalPlan?
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

    override suspend fun getNutritionalPlan( userId: String): NutritionalPlan?{

        val nutritionalPlanSnapshot = Firebase.firestore.collection("User")
            .document(userId)
            .collection("NutritionalPlan")
            .limit(1)
            .get()
            .await()

        return if (nutritionalPlanSnapshot.documents.isNotEmpty()) {
            val document = nutritionalPlanSnapshot.documents[0]
            val nutritionalPlan = document.toObject(NutritionalPlan::class.java)
            nutritionalPlan?.id = document.id
            nutritionalPlan
        } else {
            null
        }

    }



}