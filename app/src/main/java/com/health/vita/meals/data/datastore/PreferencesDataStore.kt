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
    val VALUE_KEY = intPreferencesKey("WaterIntake")
    val LAST_UPDATED_KEY = longPreferencesKey("WaterIntakeLastUpdated")
}

suspend fun saveValueAndTimestamp(context: Context, value: Int, timestamp: Long) {



    context.dataStore.edit { preferences ->
        Log.e("VALUE SAVED", "$value")
        Log.e("TIMESTAMP SAVED", "$timestamp")
        preferences[DataStoreKeys.VALUE_KEY] = value
        preferences[DataStoreKeys.LAST_UPDATED_KEY] = timestamp
    }
}

fun getValueAndTimestamp(context: Context): Flow<Pair<Int, Long>> {
    return context.dataStore.data.map { preferences ->
        val value = preferences[DataStoreKeys.VALUE_KEY] ?: 0
        val lastUpdated = preferences[DataStoreKeys.LAST_UPDATED_KEY] ?: 0L
        Pair(value, lastUpdated)
    }
}
