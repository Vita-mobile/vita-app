package com.health.vita.meals.data.data_source

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.health.vita.meals.domain.model.Ingredient
import com.health.vita.meals.domain.model.IngredientMeal
import com.health.vita.meals.domain.model.NutritionalPlan
import kotlinx.coroutines.tasks.await

interface NutritionalPlanService {
    suspend fun createNutritionalPlan(nutritionalPlan: NutritionalPlan, userId: String): Boolean
    suspend fun getRestrictions(userId: String): List<Ingredient>
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