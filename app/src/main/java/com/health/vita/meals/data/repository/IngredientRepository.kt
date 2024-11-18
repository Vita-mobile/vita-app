package com.health.vita.meals.data.repository;

import com.health.vita.meals.data.data_source.IngredientService
import com.health.vita.meals.data.data_source.IngredientServiceImpl
import com.health.vita.meals.domain.model.Ingredient

interface IngredientRepository {
    suspend fun getIngredients(): List<Ingredient?>
}

class IngredientRepositoryImpl(
    private val ingredientService: IngredientService = IngredientServiceImpl()
) : IngredientRepository{
    override suspend fun getIngredients(): List<Ingredient?> {
        return ingredientService.getIngredients()
    }

}
