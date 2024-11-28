package com.health.vita.meals.presentation.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.health.vita.meals.data.repository.DietsPreviewRepositoryImpl
import com.health.vita.meals.data.repository.MealDetailRepositoryImpl
import com.health.vita.meals.data.repository.MealsRepositoryImpl

class MealDetailViewModelFactory(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealDetailViewModel(
            mealDetailRepository= MealDetailRepositoryImpl(),
            mealPreviewRepository =  DietsPreviewRepositoryImpl(),
            mealsRepository = MealsRepositoryImpl()
        ) as T
    }
}