package com.health.vita.meals.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.meals.data.repository.IngredientRepository
import com.health.vita.meals.domain.model.Ingredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IngredientViewModel(
    private val ingredientRepository: IngredientRepository = IngredientRepositoryImpl()
) : ViewModel() {
    private val _ingredientsState = MutableLiveData<List<Ingredient?>>()
    val ingredientsState: LiveData<List<Ingredient?>> get() = _ingredientsState

    fun getMessages(otherUserID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val messages = ingredientRepository.getIngredients()
            withContext(Dispatchers.Main) { _ingredientsState.value = messages }
        }
    }
}