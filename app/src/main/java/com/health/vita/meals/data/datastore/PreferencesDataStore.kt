package com.health.vita.meals.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "persisted_data")

object DataStoreKeys {
    val HYDRATION = "HYDRATION"
    val IA_REFETCH = "IA_REFETCH"
    val HYDRATION_VALUE_KEY = intPreferencesKey("WaterIntake")
    val IA_REFETCH_VALUE_KEY = intPreferencesKey("IaRefetch")
    val HYDRATION_LAST_UPDATED_KEY = longPreferencesKey("WaterIntakeLastUpdated")
    val IA_REFETCH_LAST_UPDATED_KEY = longPreferencesKey("IaRefetchLastUpdated")
}

suspend fun saveValueAndTimestamp(context: Context, value: Int, timestamp: Long, valueKey: String ) {

    context.dataStore.edit { preferences ->
        val (valuePreferenceKey, lastUpdatedPreferenceKey) = when (valueKey) {
            "HYDRATION" -> Pair(DataStoreKeys.HYDRATION_VALUE_KEY, DataStoreKeys.HYDRATION_LAST_UPDATED_KEY)
            "IA_REFETCH" -> Pair(DataStoreKeys.IA_REFETCH_VALUE_KEY, DataStoreKeys.IA_REFETCH_LAST_UPDATED_KEY)
            else -> throw IllegalArgumentException("Unknown valueKey: $valueKey")
        }
        Log.e("VALUE SAVED", "$value")
        Log.e("TIMESTAMP SAVED", "$timestamp")
        preferences[valuePreferenceKey] = value
        preferences[lastUpdatedPreferenceKey] = timestamp
    }
}

fun getValueAndTimestamp(context: Context, valueKey: String): Flow<Pair<Int, Long>> {
    return context.dataStore.data.map { preferences ->
        val (valuePreferenceKey, lastUpdatedPreferenceKey) = when (valueKey) {
            "HYDRATION" -> Pair(DataStoreKeys.HYDRATION_VALUE_KEY, DataStoreKeys.HYDRATION_LAST_UPDATED_KEY)
            "IA_REFETCH" -> Pair(DataStoreKeys.IA_REFETCH_VALUE_KEY, DataStoreKeys.IA_REFETCH_LAST_UPDATED_KEY)
            else -> throw IllegalArgumentException("Unknown valueKey: $valueKey")
        }
        val value = preferences[valuePreferenceKey] ?: 0
        val lastUpdated = preferences[lastUpdatedPreferenceKey] ?: 0L
        Pair(value, lastUpdated)
    }
}
