package com.health.vita.meals.presentation.viewModels

import DietsPreviewViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.health.vita.meals.data.repository.DietsPreviewRepositoryImpl
import com.health.vita.meals.data.repository.MealsRepositoryImpl

class DietsPreviewViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DietsPreviewViewModel(
            context = context,
            dietsPreviewRepository = DietsPreviewRepositoryImpl(),
            mealsRepository = MealsRepositoryImpl(context)
        ) as T
    }
}