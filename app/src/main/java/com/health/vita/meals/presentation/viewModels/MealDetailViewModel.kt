package com.health.vita.meals.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.health.vita.meals.domain.model.Meal

class MealDetailViewModel() : ViewModel() {
    private val _pesoTotal = MutableLiveData<Int>(0)
    val pesoTotal: LiveData<Int> get() = _pesoTotal

    fun calcularPesoTotal(comida: Meal) {
        _pesoTotal.value = comida.ingredients.sumOf { it.grams.toInt() }
    }
}