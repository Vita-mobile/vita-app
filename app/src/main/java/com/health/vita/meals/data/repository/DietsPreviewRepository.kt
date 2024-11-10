package com.health.vita.meals.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.health.vita.meals.data.data_source.DietsPreviewService
import com.health.vita.meals.data.data_source.DietsPreviewServiceImpl
import com.health.vita.meals.data.data_source.MealsIAService
import com.health.vita.meals.data.data_source.MealsIAServiceImpl
import com.health.vita.meals.data.data_source.MealsService
import com.health.vita.meals.data.data_source.MealsServiceImpl
import com.health.vita.meals.domain.model.Meal

interface DietsPreviewRepository{

    //suspend fun rechargeMealIA(): Boolean
    suspend fun generateMealsIA(): Boolean
    suspend fun getMealsIA(meal: Int): List<Meal>
    suspend fun getFavorites(): List<Meal>
}

class DietsPreviewRepositoryImpl(
    private val dietsPreviewService: DietsPreviewService = DietsPreviewServiceImpl(),
    private val mealsIAService: MealsIAService = MealsIAServiceImpl(),
    private val mealsService: MealsService = MealsServiceImpl()
): DietsPreviewRepository {
    override suspend fun generateMealsIA(): Boolean {
        return try {
            Firebase.auth.currentUser?.let { user ->
                val mealsQuantity: Int = mealsService.getMealsQuantity(user.uid)
                val meals: List<Meal> = mealsIAService.getMeals(user.uid, mealsQuantity)

                dietsPreviewService.generateMealsIA(user.uid, meals)
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    override suspend fun getMealsIA(meal: Int): List<Meal> {
        return try {
            Firebase.auth.currentUser?.let { user ->
                dietsPreviewService.getMealsIA(user.uid)
                    .filter { it.meal == meal }
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getFavorites(): List<Meal> {
        return try {
            Firebase.auth.currentUser?.let { user ->
                dietsPreviewService.getFavorites(user.uid)
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


}