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
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dietsPreviewRepository = DietsPreviewRepositoryImpl()
        val mealsRepository = MealsRepositoryImpl()

        return when {
            modelClass.isAssignableFrom(IADietsPreviewViewModel::class.java) -> {
                IADietsPreviewViewModel(
                    dietsPreviewRepository = dietsPreviewRepository,
                    mealsRepository = mealsRepository
                ) as T
            }

            modelClass.isAssignableFrom(FavoritesDietsPreviewViewModel::class.java) -> {
                FavoritesDietsPreviewViewModel(
                    dietsPreviewRepository = dietsPreviewRepository,
                    mealsRepository = mealsRepository
                ) as T
            }

            modelClass.isAssignableFrom(CreationsDietsPreviewViewModel::class.java) -> {
                CreationsDietsPreviewViewModel(
                    dietsPreviewRepository = dietsPreviewRepository,
                    mealsRepository = mealsRepository
                ) as T
            }

            modelClass.isAssignableFrom(CreateMealViewModel::class.java) -> {
                CreateMealViewModel(
                    dietsPreviewRepository = dietsPreviewRepository,
                    mealsRepository = mealsRepository
                ) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
