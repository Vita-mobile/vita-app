package com.health.vita.meals.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.health.vita.meals.data.data_source.DietsPreviewService
import com.health.vita.meals.data.data_source.DietsPreviewServiceImpl
import com.health.vita.meals.data.data_source.MealsIAService
import com.health.vita.meals.data.data_source.MealsIAServiceImpl
import com.health.vita.meals.domain.model.IngredientMeal
import com.health.vita.meals.domain.model.Meal

interface DietsPreviewRepository{

    suspend fun rechargeMealIA(meal: Int): Boolean
    suspend fun generateMealsIA(mealsQuantity: Int): Boolean
    suspend fun getMealsIA(meal: Int): List<Meal>
    suspend fun getFavorites(): List<Meal>
    suspend fun consumeMeal(meal: Meal): Boolean
    suspend fun createMeal(meal: Meal): Boolean
}

class DietsPreviewRepositoryImpl(
    private val dietsPreviewService: DietsPreviewService = DietsPreviewServiceImpl(),
    private val mealsIAService: MealsIAService = MealsIAServiceImpl(),
): DietsPreviewRepository {
    override suspend fun generateMealsIA(mealsQuantity: Int): Boolean {
        return try {
            Firebase.auth.currentUser?.let { user ->
                val meals: List<Meal> = mealsIAService.getMeals(user.uid, mealsQuantity)

                val updatedMeals = meals.mapIndexed { index, meal ->
                    val mealNumber = (index) / 3 + 1
                    meal.copy(meal = mealNumber)
                }

                dietsPreviewService.generateMealsIA(user.uid, updatedMeals)
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

    override suspend fun consumeMeal(meal: Meal): Boolean {
        return try {
            Firebase.auth.currentUser?.let { user ->
                dietsPreviewService.consumeMeal(user.uid, meal)
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun createMeal(meal: Meal): Boolean {
        return try {
            Firebase.auth.currentUser?.let { user ->
                dietsPreviewService.addCreatedMeal(user.uid, meal)
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun rechargeMealIA(meal: Int): Boolean {
        return try {
            Firebase.auth.currentUser?.let { user ->
                val isRemoved = dietsPreviewService.removeMealIA(user.uid, meal)

                if (!isRemoved) {
                    return false
                }

                val meals: List<Meal> = mealsIAService.getMeals(user.uid, 1)

                if (meals.isNotEmpty()) {
                    val updatedMeals = meals.map { mealItem ->
                        mealItem.copy(meal = meal)
                    }
                    return dietsPreviewService.generateMealsIA(user.uid, updatedMeals)
                } else {
                    return false
                }
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


}