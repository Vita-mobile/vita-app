package com.health.vita.meals.presentation.viewModels

import CreationsDietsPreviewViewModel
import FavoritesDietsPreviewViewModel
import IADietsPreviewViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.health.vita.meals.data.repository.DietsPreviewRepositoryImpl
import com.health.vita.meals.data.repository.MealsRepositoryImpl

class DietsPreviewViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dietsPreviewRepository = DietsPreviewRepositoryImpl()
        val mealsRepository = MealsRepositoryImpl(context)

        return when {
            modelClass.isAssignableFrom(IADietsPreviewViewModel::class.java) -> {
                IADietsPreviewViewModel(
                    context = context,
                    dietsPreviewRepository = dietsPreviewRepository,
                    mealsRepository = mealsRepository
                ) as T
            }

            modelClass.isAssignableFrom(FavoritesDietsPreviewViewModel::class.java) -> {
                FavoritesDietsPreviewViewModel(
                    context = context,
                    dietsPreviewRepository = dietsPreviewRepository,
                    mealsRepository = mealsRepository
                ) as T
            }

            modelClass.isAssignableFrom(CreationsDietsPreviewViewModel::class.java) -> {
                CreationsDietsPreviewViewModel(
                    context = context,
                    dietsPreviewRepository = dietsPreviewRepository,
                    mealsRepository = mealsRepository
                ) as T
            }

            modelClass.isAssignableFrom(CreateMealViewModel::class.java) -> {
                CreateMealViewModel(
                    context = context,
                    dietsPreviewRepository = dietsPreviewRepository,
                    mealsRepository = mealsRepository
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
