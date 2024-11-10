package com.health.vita.meals.data.repository

import com.health.vita.meals.data.data_source.NutritionalPlanService
import com.health.vita.meals.data.data_source.NutritionalPlanServiceImpl
import com.health.vita.meals.domain.model.Ingredient
import com.health.vita.meals.domain.model.NutritionalPlan
import com.health.vita.profile.data.repository.UserRepository
import com.health.vita.profile.data.repository.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID

interface NutritionalPlanRepository {
    suspend fun createNutritionalPlan(preferences : List<Ingredient>, restrictions: List<Ingredient> )
}

class NutritionalPlanRepositoryImpl(
    private val nutritionalPlanService: NutritionalPlanService = NutritionalPlanServiceImpl(),
    private val userRepo: UserRepository = UserRepositoryImpl()
) : NutritionalPlanRepository {

    override suspend fun createNutritionalPlan(
        preferences: List<Ingredient>,
        restrictions: List<Ingredient>
    ) {

        val user = userRepo.getCurrentUser()

        user?.let {
            val nutritionalPlan = NutritionalPlan(
                id = UUID.randomUUID().toString(),
                carbs = 150f,
                fats = 50f,
                proteins = 70f,
                hydrationLevel = 2.0f,
                kcalGoal = 2000f,
                meals = 5,
                registerPlanDate = LocalDate.now(),
                preferences = preferences,
                restrictions = restrictions
            )
        } ?: run {

        }



    }

}