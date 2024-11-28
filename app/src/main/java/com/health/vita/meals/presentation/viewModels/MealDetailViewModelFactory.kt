package com.health.vita.meals.presentation.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.health.vita.meals.data.repository.DietsPreviewRepositoryImpl
import com.health.vita.meals.data.repository.MealDetailRepositoryImpl
import com.health.vita.meals.data.repository.MealsRepositoryImpl

class MealDetailViewModelFactory(
    private val contexto: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealDetailViewModel(
            contex = contexto,
            mealDetailRepository= MealDetailRepositoryImpl(),
            mealPreviewRepository =  DietsPreviewRepositoryImpl(),
            mealsRepository = MealsRepositoryImpl(contexto)
        ) as T
    }
}