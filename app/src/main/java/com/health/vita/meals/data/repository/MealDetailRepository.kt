package com.health.vita.meals.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.health.vita.meals.data.data_source.MealDetailService
import com.health.vita.meals.data.data_source.MealDetailServiceImp
import com.health.vita.meals.domain.model.Meal

interface MealDetailRepository {
    suspend fun toggleFavorite(meal: Meal, isFav: Boolean): Unit
}



class MealDetailRepositoryImpl(
    val mealsDetailService: MealDetailService = MealDetailServiceImp(),
    ): MealDetailRepository {
    override suspend fun toggleFavorite(meal: Meal, isFav: Boolean) {
        if (isFav){
            mealsDetailService.addFavMeal(meal, Firebase.auth.currentUser?.uid?:"")
        }else{
            mealsDetailService.removeFavMeal(meal.id, Firebase.auth.currentUser?.uid?:"")
        }
    }

}
