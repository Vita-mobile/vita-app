package com.health.vita.meals.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.meals.data.data_source.MealsService
import com.health.vita.meals.data.data_source.MealsServiceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface MealsRepository {
    suspend fun getLastMealIndex(): Int
    suspend fun incrementMealIndex()
    suspend fun getMealsCount(): Int
    suspend fun resetMealIndex()
    suspend fun getLastEatenMealDate(): Timestamp
}

class MealsRepositoryImpl(
    val mealsService: MealsService = MealsServiceImpl(),
    val mealsRepository: MealTrackingRepositoryImpl = MealTrackingRepositoryImpl(),


) : MealsRepository {

    override suspend fun getLastMealIndex():Int{
        return mealsService.getLastMealIndex(Firebase.auth.currentUser?.uid ?: "")
    }

    override suspend fun incrementMealIndex() {
        return mealsService.incrementMealIndex(Firebase.auth.currentUser?.uid ?: "")
    }

    override suspend fun getMealsCount(): Int {
        return mealsService.getMealsQuantity(Firebase.auth.currentUser?.uid ?: "")
    }

    override suspend fun resetMealIndex() {
        mealsService.resetMealIndex(Firebase.auth.currentUser?.uid ?: "")
    }

    override suspend fun getLastEatenMealDate(): Timestamp {
        return mealsRepository.getLastEatenMealDate()
    }
}
