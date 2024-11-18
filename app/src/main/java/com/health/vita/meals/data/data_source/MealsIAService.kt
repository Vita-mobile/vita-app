package com.health.vita.meals.data.data_source
import com.google.firebase.auth.FirebaseAuth
import com.health.vita.meals.domain.model.Meal
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface MealsIAServiceRetrofit{
    @GET("generate-recipes/{userId}/{mealsQuantity}")
    suspend fun getMeals(
        @Path("userId") userId: String,
        @Path("mealsQuantity") mealsQuantity: Int,
        @Header("Authorization") token: String,
        ): List<Meal>
}

interface MealsIAService {
    suspend fun getMeals(userId: String, mealsQuantity: Int): List<Meal>
}

class MealsIAServiceImpl(): MealsIAService {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://vita-app-88d5537bb869.herokuapp.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(MealsIAServiceRetrofit::class.java)

    override suspend fun getMeals(userId: String, mealsQuantity: Int ): List<Meal> {
        val token = obtainFirebaseToken()

        if (token != null) {
            return service.getMeals(userId, mealsQuantity, "Bearer $token")
        } else {
            throw Exception("No se pudo obtener el token de Firebase")
        }
    }

    private suspend fun obtainFirebaseToken(): String {
        val user = FirebaseAuth.getInstance().currentUser
        val tokenResult = user?.getIdToken(true)?.await()
        return tokenResult?.token ?: throw Exception("No se pudo obtener el token de Firebase")
    }
}