package com.health.vita.meals.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.meals.data.repository.IngredientRepository
import com.health.vita.meals.data.repository.IngredientRepositoryImpl
import com.health.vita.meals.domain.model.IngredientMeal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IngredientViewModel(
    private val ingredientRepository: IngredientRepository = IngredientRepositoryImpl()
) : ViewModel() {
    private val _ingredientsState = MutableLiveData<List<IngredientMeal?>>()
    val ingredientsState: LiveData<List<IngredientMeal?>> get() = _ingredientsState

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    fun getIngredients() {
        viewModelScope.launch(Dispatchers.IO) {
            val messages = ingredientRepository.getIngredients()
            withContext(Dispatchers.Main) { _ingredientsState.value = messages }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}