package com.health.vita.meals.data.data_source

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.health.vita.meals.domain.model.Meal
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

interface MealTrackingService {


    suspend fun getNutritionalPlanCreationDate(id: String): Long

    suspend fun getMealsOfADate(id: String, date: LocalDate): List<Meal>
    suspend fun geatLastEatenMealDate(s: String): Timestamp?

}

class MealTrackingServiceImpl() : MealTrackingService {

    override suspend fun getNutritionalPlanCreationDate(id: String): Long {

        val nutritionalPlanSnapShot = Firebase.firestore
            .collection("User")
            .document(id).collection("NutritionalPlan").get().await()

        Log.d("Meals tracking service", "Nutritional plan snapshot: $nutritionalPlanSnapShot")

        Log.d(
            "Meals tracking service",
            "Nutritional plan snapshot documents: ${nutritionalPlanSnapShot.documents.size}"
        )


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

    override suspend fun getMealsOfADate(id: String, date: LocalDate): List<Meal> {
    Log.d("Meals tracking service", "Date on service: $date")
    Log.d("Meals tracking service", "User ID $id")

    val startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
    val endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()

    val mealsByDateQuery = Firebase.firestore
        .collection("User")
        .document(id)
        .collection("MealsTracking")
        .whereGreaterThanOrEqualTo("consumeDate", Timestamp(startOfDay.epochSecond, startOfDay.nano))
        .whereLessThan("consumeDate", Timestamp(endOfDay.epochSecond, endOfDay.nano))
        .get()
        .await()

    val meals = mealsByDateQuery.documents.mapNotNull { mealDocument ->
        mealDocument.toObject(Meal::class.java)
    }

    Log.d("Meals tracking service", "Meals found: $meals")
    return meals
}

    override suspend fun geatLastEatenMealDate(s: String): Timestamp? {
        val mealTracking = Firebase.firestore
            .collection("User")
            .document(s)
            .collection("MealsTracking")
            .get()
            .await()
        val lastEatenMealDoc = mealTracking.documents.firstOrNull()?.
        reference?.
        collection("TrackMeals")?.
        orderBy("date", Query.Direction.DESCENDING)?.
        limit(1)?.
        get()?.
        await()
        return lastEatenMealDoc?.documents?.firstOrNull()?.get("date") as? Timestamp
    }

}