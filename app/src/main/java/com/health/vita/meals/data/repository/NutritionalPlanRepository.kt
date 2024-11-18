package com.health.vita.meals.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.health.vita.core.utils.DatabaseNames
import com.health.vita.meals.data.data_source.NutritionalPlanService
import com.health.vita.meals.data.data_source.NutritionalPlanServiceImpl
import com.health.vita.meals.domain.model.Ingredient
import com.health.vita.meals.domain.model.IngredientMeal
import com.health.vita.meals.domain.model.NutritionalPlan
import com.health.vita.meals.utils.MacroPercentages
import com.health.vita.profile.data.repository.UserRepository
import com.health.vita.profile.data.repository.UserRepositoryImpl
import java.util.UUID
import kotlin.reflect.KMutableProperty1

interface NutritionalPlanRepository {
    suspend fun createNutritionalPlan(
        preferences: List<IngredientMeal>, restrictions: List<IngredientMeal>, meals: Int
    ): Boolean

    suspend fun getNutritionalPlan(): NutritionalPlan?
    suspend fun updateNutritionalPlan(
        updates: Map<String, Any?>
    ): Boolean
    suspend fun getRestrictions(): List<Ingredient>

}

class NutritionalPlanRepositoryImpl(
    private val nutritionalPlanService: NutritionalPlanService = NutritionalPlanServiceImpl(),
    private val userRepo: UserRepository = UserRepositoryImpl()
) : NutritionalPlanRepository {

    override suspend fun createNutritionalPlan(
        preferences: List<IngredientMeal>, restrictions: List<IngredientMeal>, meals: Int
    ): Boolean {

        val user = userRepo.getCurrentUser()

        user?.let {

            val nutritionalPlan = generateNutritionalPlan(
                physicalLevel = it.physicalLevel,
                sex = it.sex,
                weight = it.weight,
                height = it.height,
                age = it.age,
                physicalTarget = it.physicalTarget,
                preferences = preferences,
                restrictions = restrictions,
                meals = meals
            )

            return nutritionalPlanService.createNutritionalPlan(nutritionalPlan, it.id)

        } ?: run {
            Log.d("Meal tracking repository", "No currentUser provided.")
            return false
        }

    }

    override suspend fun getNutritionalPlan(): NutritionalPlan? {

        val user = userRepo.getCurrentUser()

        val nutritionalPlan = nutritionalPlanService.getNutritionalPlan(user?.id ?: "")

        return nutritionalPlan
    }

    override suspend fun updateNutritionalPlan(
        updates: Map<String, Any?>
    ): Boolean {
        val user = userRepo.getCurrentUser()

        val nutritionalPlan = nutritionalPlanService.getNutritionalPlan(user?.id ?: "")

        nutritionalPlan?.let { currentPlan ->

            val updatedPlan = generateNutritionalPlan(
                id = currentPlan.id,
                physicalLevel = (updates["physicalLevel"] as? Number)?.toInt()
                    ?: (user?.physicalLevel ?: 1),
                sex = (updates["sex"] as? String)
                    ?: (user?.sex ?: "Masculino"),
                weight = (updates["weight"] as? Number)?.toFloat()
                    ?: (user?.weight ?: 0f),
                height = (updates["height"] as? Number)?.toFloat()
                    ?: (user?.height ?: 0f),
                age = (updates["age"] as? Number)?.toInt()
                    ?: (user?.age ?: 0),
                physicalTarget = ((updates["physicalTarget"] as? String) ?: (user?.physicalTarget
                    ?: DatabaseNames.physicalTarget[2])).toString(),
                preferences = (updates["preferences"] as? List<IngredientMeal>)
                    ?: (currentPlan.preferences ?: emptyList()),
                restrictions = (updates["restrictions"] as? List<IngredientMeal>)
                    ?: (currentPlan.restrictions ?: emptyList()),
                meals = (updates["meals"] as? Number)?.toInt()
                    ?: (currentPlan.meals ?: 3)
            )


            return nutritionalPlanService.editNutritionalPlan(updatedPlan, user?.id ?: "")

        } ?: run {

            return false

        }

    }

    fun generateNutritionalPlan(
        id: String = UUID.randomUUID().toString(),
        physicalLevel: Int,
        sex: String,
        weight: Float,
        height: Float,
        age: Int,
        physicalTarget: String,
        preferences: List<IngredientMeal>,
        restrictions: List<IngredientMeal>,
        meals: Int
    ): NutritionalPlan {
        var cal = 0f
        val level = when (physicalLevel) {
            1 -> 1.2f
            2 -> 1.375f
            3 -> 1.55f
            4 -> 1.725f
            5 -> 1.9f
            else -> 1.2f
        }

        if (sex == "Masculino") {
            cal = (10f * (weight ?: 0f)) + (6.25f * (height ?: 0f)) - (5 * (age ?: 0)) + 5
        } else if (sex == "Femenino") {
            cal = (10f * (weight ?: 0f)) + (6.25f * (height ?: 0f)) - (5 * (age ?: 0)) - 161
        }
        cal = (cal * level)

        var carbs = 0f
        var fats = 0f
        var proteins = 0f
        val hydrationLevel = 35 * weight

        when (physicalTarget) {
            DatabaseNames.physicalTarget[1] -> {
                cal -= 200
                carbs = cal * MacroPercentages.Definition.CARBS
                fats = cal * MacroPercentages.Definition.FATS
                proteins = cal * MacroPercentages.Definition.PROTEINS
            }

            DatabaseNames.physicalTarget[3] -> {
                cal += 200
                carbs = cal * MacroPercentages.Volume.CARBS
                fats = cal * MacroPercentages.Volume.FATS
                proteins = cal * MacroPercentages.Volume.PROTEINS
            }

            else -> {
                carbs = cal * MacroPercentages.Maintain.CARBS
                fats = cal * MacroPercentages.Maintain.FATS
                proteins = cal * MacroPercentages.Maintain.PROTEINS
            }
        }

        val nutritionalPlan = NutritionalPlan(
            id = id,
            carbs = carbs,
            fats = fats,
            proteins = proteins,
            hydrationLevel = hydrationLevel,
            kcalGoal = cal,
            meals = meals,
            registerPlanDate = Timestamp.now(),
            preferences = preferences,
            restrictions = restrictions
        )
        return nutritionalPlan
    }


    override suspend fun getRestrictions(): List<Ingredient>{
        return try {
            Firebase.auth.currentUser?.let { user ->
                nutritionalPlanService.getRestrictions(user.uid)
            } ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}