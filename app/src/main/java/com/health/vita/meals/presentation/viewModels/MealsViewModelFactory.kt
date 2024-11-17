package com.health.vita.meals.presentation.viewModels

import MealsViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.health.vita.meals.data.repository.MealsRepository

import com.health.vita.meals.data.repository.MealsRepositoryImpl

import com.health.vita.profile.data.repository.UserRepositoryImpl

class MealsViewModelFactory (
    private val context: Context
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealsViewModel::class.java)) {
            return MealsViewModel(
                context = context,
                userRepository = UserRepositoryImpl(),
                mealsRepository = MealsRepositoryImpl(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }


}