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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "storeData")

interface MealsRepository {
    fun getLastMealIndex(): Flow<Int>
    suspend fun incrementMealIndex()
    suspend fun getMealsCount(): Int
    suspend fun resetMealIndex()
    suspend fun getLastEatenMealDate(): Timestamp
}

class MealsRepositoryImpl(
    private val context: Context,
    val mealsService: MealsService = MealsServiceImpl(),
    val mealsRepository: MealTrackingRepositoryImpl = MealTrackingRepositoryImpl(),


) : MealsRepository {

    companion object {
        val LAST_MEAL_INDEX = intPreferencesKey("last_meal_index")
    }

    override fun getLastMealIndex(): Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_MEAL_INDEX] ?: 0
        }

    override suspend fun incrementMealIndex() {
        context.dataStore.edit { preferences ->
            val currentIndex = preferences[LAST_MEAL_INDEX] ?: 0
            preferences[LAST_MEAL_INDEX] = currentIndex + 1
        }
    }

    override suspend fun getMealsCount(): Int {
        return mealsService.getMealsQuantity(Firebase.auth.currentUser?.uid ?: "")
    }

    override suspend fun resetMealIndex() {
        context.dataStore.edit { preferences -> preferences[LAST_MEAL_INDEX] = 0 }
    }

    override suspend fun getLastEatenMealDate(): Timestamp {
        return mealsRepository.getLastEatenMealDate()
    }
}
