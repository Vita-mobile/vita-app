package com.health.vita.meals.data.data_source
import com.health.vita.meals.domain.model.Meal
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

interface MealsIAService{
    @GET("generate-recipes/{userId}/{mealsQuantity}")
    suspend fun getMeals(
        @Path("userId") userId: String,
        @Path("mealsQuantity") mealsQuantity: Int
    ): List<Meal>
}


class MealsIAServiceImpl(): MealsIAService {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.17:5000/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(MealsIAService::class.java)

    override suspend fun getMeals(userId: String, mealsQuantity: Int): List<Meal> {
        return try {
            service.getMeals(userId, mealsQuantity)
        } catch (e: SocketTimeoutException) {
            throw Exception("Ha tardado demasiado tiempo en responder")
        } catch (e: Exception) {
            throw Exception("Ha ocurrido un error en la solicitud")
        }
    }


}