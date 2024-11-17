package com.health.vita.meals.data.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.health.vita.core.utils.DatabaseNames
import com.health.vita.meals.data.data_source.NutritionalPlanService
import com.health.vita.meals.data.data_source.NutritionalPlanServiceImpl
import com.health.vita.meals.domain.model.Ingredient
import com.health.vita.meals.domain.model.NutritionalPlan
import com.health.vita.meals.utils.MacroPercentages
import com.health.vita.profile.data.repository.UserRepository
import com.health.vita.profile.data.repository.UserRepositoryImpl
import java.util.UUID

interface NutritionalPlanRepository {
    suspend fun createNutritionalPlan(preferences: List<Ingredient>, restrictions: List<Ingredient>, meals: Int): Boolean
}

class NutritionalPlanRepositoryImpl(
    private val nutritionalPlanService: NutritionalPlanService = NutritionalPlanServiceImpl(),
    private val userRepo: UserRepository = UserRepositoryImpl()
) : NutritionalPlanRepository {

    override suspend fun createNutritionalPlan(
        preferences: List<Ingredient>,
        restrictions: List<Ingredient>,
        meals: Int
    ) : Boolean {

        val user = userRepo.getCurrentUser()

        user?.let {

            var cal = 0f
            val level = when (it.physicalLevel) {
                1 -> 1.2f
                2 -> 1.375f
                3 -> 1.55f
                4 -> 1.725f
                5 -> 1.9f
                else -> 1.2f
            }

            if (it.sex == "Masculino") {
                cal = (10f * (it.weight ?: 0f)) + (6.25f * (it.height ?: 0f)) - (5 * (it.age
                    ?: 0)) + 5
            } else if (it.sex == "Femenino") {
                cal = (10f * (it.weight ?: 0f)) + (6.25f * (it.height ?: 0f)) - (5 * (it.age
                    ?: 0)) - 161
            }
            cal = (cal * level)

            var carbs = 0f
            var fats = 0f
            var proteins = 0f
            val hydrationLevel = 35 * it.weight

            when (it.physicalTarget) {
                DatabaseNames.physicalTarget[1] -> {
                    carbs = cal * MacroPercentages.Definition.CARBS
                    fats = cal * MacroPercentages.Definition.FATS
                    proteins = cal * MacroPercentages.Definition.PROTEINS
                }
                DatabaseNames.physicalTarget[3] -> {
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
                id = UUID.randomUUID().toString(),
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

            return nutritionalPlanService.createNutritionalPlan(nutritionalPlan, it.id)

        } ?: run {
            Log.d("Meal tracking repository", "No currentUser provided.")
            return false
        }


    }

}