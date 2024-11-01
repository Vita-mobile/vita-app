package com.health.vita.meals.data.data_source

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

interface MealTrackingService {


    suspend fun getNutritionalPlanCreationDate(id: String): Long

}

class MealTrackingServiceImpl() : MealTrackingService {

    override suspend fun getNutritionalPlanCreationDate(id: String): Long {

        val nutritionalPlanSnapShot = Firebase.firestore
            .collection("User")
            .document(id).collection("NutritionalPlan").get().await()

        Log.d("Meals tracking service", "Nutritional plan snapshot: $nutritionalPlanSnapShot")

        Log.d("Meals tracking service", "Nutritional plan snapshot documents: ${nutritionalPlanSnapShot.documents.size}")


        val nutritionalPlan = nutritionalPlanSnapShot.documents.firstOrNull()



        Log.d("Meals tracking service", "Nutritional plan found: $nutritionalPlan")

        val timeStamp = nutritionalPlan?.getTimestamp("registerPlanDate")

        timeStamp?.let {

            val seconds = it.seconds

            Log.d("Meals tracking service", "Seconds: $seconds")
            return seconds

        } ?: run {
            Log.d("Meals tracking service", "Not timestamp found.")
            return -1

        }


    }
}