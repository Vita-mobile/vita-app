package com.health.vita.meals.data.data_source

import android.util.Log
import androidx.compose.runtime.snapshots.Snapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.health.vita.meals.domain.model.Ingredient
import com.health.vita.meals.domain.model.IngredientMeal
import com.health.vita.domain.model.User
import com.health.vita.meals.domain.model.NutritionalPlan
import kotlinx.coroutines.tasks.await

interface NutritionalPlanService {
    suspend fun createNutritionalPlan(nutritionalPlan: NutritionalPlan, userId: String): Boolean
    suspend fun getRestrictions(userId: String): List<Ingredient>
    suspend fun getNutritionalPlan(userId: String): NutritionalPlan?
    suspend fun editNutritionalPlan(updatedPlan: NutritionalPlan, userId: String): Boolean
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

    override suspend fun editNutritionalPlan(updatedPlan: NutritionalPlan, userId: String): Boolean {
        return try {
            Firebase.firestore
                .collection("User")
                .document(userId)
                .collection("NutritionalPlan")
                .document(updatedPlan.id)
                .set(updatedPlan)
                .await()

            true
        } catch (e: Exception) {
            Log.e(">>>", "Error durante la actualización: ${e.message}")
            false
        }
    }


    override suspend fun getRestrictions(userId: String): List<Ingredient> {
        return try {
            val nutritionalPlanCollection = Firebase.firestore
                .collection("User")
                .document(userId)
                .collection("NutritionalPlan")
                .get()
                .await()

            val document = nutritionalPlanCollection.documents.firstOrNull() ?: return emptyList()

            val restrictions = document["restrictions"] as? List<*> ?: return emptyList()

            restrictions.mapNotNull { restriction ->
                try {
                    val json = Gson().toJson(restriction)
                    Gson().fromJson(json, Ingredient::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}