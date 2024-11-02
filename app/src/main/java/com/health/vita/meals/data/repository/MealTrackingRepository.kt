package com.health.vita.meals.data.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.meals.data.data_source.MealTrackingService
import com.health.vita.meals.data.data_source.MealTrackingServiceImpl
import com.health.vita.meals.domain.model.Meal
import java.time.LocalDate

interface MealTrackingRepository {

    suspend fun getTrackingDateRange(): Int;

    suspend fun getMealsOfADate(date: LocalDate): List<Meal>
    suspend fun getLastEatenMealDate(): Timestamp

}

class MealTrackingRepositoryImpl(

    private val mealTrackingService: MealTrackingService = MealTrackingServiceImpl()

) : MealTrackingRepository {
    override suspend fun getTrackingDateRange(): Int {


        Firebase.auth.currentUser?.let {

            val nutritionalPlanCreationDate =
                mealTrackingService.getNutritionalPlanCreationDate(it.uid)


            if (nutritionalPlanCreationDate == -1L) {
                Log.d("Meal tracking repository", "No nutritional plan creation date found.")
                return -1
            }


            val secondsDifference = System.currentTimeMillis() / 1000 - nutritionalPlanCreationDate


            Log.d("Meal tracking repository", "Date difference.$secondsDifference")

            // Now obtain the number of days from the seconds difference

            return (secondsDifference / (24 * 3600)).toInt()


        } ?: run {

            Log.d("Meal tracking repository", "No currentUser provided.")
            return -1

        }
    }

    override suspend fun getMealsOfADate(date: LocalDate): List<Meal> {

        Firebase.auth.currentUser?.let {

            val meals = mealTrackingService.getMealsOfADate(it.uid, date)

            return meals


        } ?: run {

            Log.d("Meal tracking repository", "No currentUser provided for getMealsOfDate.")
            return emptyList()
        }


    }

    override suspend fun getLastEatenMealDate(): Timestamp {
        return mealTrackingService.geatLastEatenMealDate(Firebase.auth.currentUser?.uid?:"")?:Timestamp.now()
    }


}