package com.health.vita.meals.presentation.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "storeData")

interface MealsRepository {
    fun getLastMealIndex(): Flow<Int>
    suspend fun incrementMealIndex()
}

class MealsRepositoryImpl(private val context: Context) : MealsRepository {

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
}
