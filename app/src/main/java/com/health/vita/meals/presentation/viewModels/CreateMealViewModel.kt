package com.health.vita.meals.presentation.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.error_management.DatabaseError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.repository.DietsPreviewRepository
import com.health.vita.meals.data.repository.DietsPreviewRepositoryImpl
import com.health.vita.meals.data.repository.IngredientRepository
import com.health.vita.meals.data.repository.IngredientRepositoryImpl
import com.health.vita.meals.data.repository.MealsRepository
import com.health.vita.meals.data.repository.MealsRepositoryImpl
import com.health.vita.meals.data.repository.NutritionalPlanRepository
import com.health.vita.meals.data.repository.NutritionalPlanRepositoryImpl
import com.health.vita.meals.domain.model.Ingredient
import com.health.vita.meals.domain.model.IngredientMeal
import com.health.vita.meals.domain.model.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateMealViewModel (
    context: Context,
    private val mealsRepository: MealsRepository = MealsRepositoryImpl(context),
    private val nutritionalPlanRepository: NutritionalPlanRepository = NutritionalPlanRepositoryImpl(),
    private val ingredientRepository: IngredientRepository = IngredientRepositoryImpl(),
    private val dietsPreviewRepository: DietsPreviewRepository = DietsPreviewRepositoryImpl()
): ViewModel() {

    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _ingredientsState = MutableLiveData<List<Ingredient?>>()
    val ingredientsState: LiveData<List<Ingredient?>> get() = _ingredientsState

    private val _addedIngredientsState = MutableLiveData<List<Pair<Ingredient, Int>>>()
    val addedIngredientsState: LiveData<List<Pair<Ingredient, Int>>> get() = _addedIngredientsState

    private val _mealName = MutableLiveData<String>("")
    val mealName: LiveData<String> get() = _mealName

    private val _lastCreatedMeal = MutableLiveData<Meal>(null)
    val lastCreatedMeal: LiveData<Meal> get() = _lastCreatedMeal

    private val _consumeMealState = MutableLiveData<Boolean>(false)
    val consumeMealState: LiveData<Boolean> = _consumeMealState

    fun setMealName(name: String) {
        _mealName.value = name
    }

    private val _mealCreationSuccess = MutableLiveData<Boolean>(false)
    val mealCreationSuccess: LiveData<Boolean> = _mealCreationSuccess

    init {
        getIngredients()
    }

    fun resetMealCreationSuccess() {
        _mealCreationSuccess.value = false
    }

    fun addIngredientToMeal(ingredient: Ingredient, grams: Int) {
        val currentIngredients = _addedIngredientsState.value?.toMutableList() ?: mutableListOf()
        currentIngredients.add(Pair(ingredient, grams))
        _addedIngredientsState.value = currentIngredients
    }

    fun removeIngredientFromMeal(ingredient: Ingredient) {
        val currentIngredients = _addedIngredientsState.value?.toMutableList() ?: mutableListOf()
        currentIngredients.removeAll { it.first == ingredient }
        _addedIngredientsState.value = currentIngredients
    }

    fun updateIngredientGram(ingredient: Ingredient, grams: Int) {
        val currentIngredients = _addedIngredientsState.value?.toMutableList() ?: mutableListOf()
        val index = currentIngredients.indexOfFirst { it.first == ingredient }

        if (index != -1) {
            currentIngredients[index] = currentIngredients[index].copy(second = grams)
            _addedIngredientsState.value = currentIngredients
        }
    }

    fun getIngredients() {
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }


            try {
                val allIngredients = ingredientRepository.getIngredients()
                val restrictions = nutritionalPlanRepository.getRestrictions()

                val ingredients = allIngredients.filterNot { it in restrictions }

                withContext(Dispatchers.Main) {
                    _ingredientsState.value = ingredients
                    _uiHandler.setSuccess()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _uiHandler.setErrorState(DatabaseError())
                }
            }
        }
    }

    fun createMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }

            val ingredients = _addedIngredientsState.value ?: emptyList()

            val totalCalories = ingredients.sumOf { (ingredient, grams) ->
                ingredient.calories * grams / 100.0
            }.toFloat()

            val totalCarbs = ingredients.sumOf { (ingredient, grams) ->
                ingredient.carbs * grams / 100.0
            }.toFloat()

            val totalFats = ingredients.sumOf { (ingredient, grams) ->
                ingredient.fats * grams / 100.0
            }.toFloat()

            val totalProteins = ingredients.sumOf { (ingredient, grams) ->
                ingredient.protein * grams / 100.0
            }.toFloat()

            val ingredientMeals = ingredients.map { (ingredient, grams) ->
                IngredientMeal(
                    grams = grams.toFloat(),
                    name = ingredient.name,
                    id = ingredient.id
                )
            }

            val meal = Meal(
                name = _mealName.value ?: "",
                calories = totalCalories,
                carbs = totalCarbs,
                fats = totalFats,
                proteins = totalProteins,
                ingredientMeals = ingredientMeals
            )

            try {

                val isCreated = dietsPreviewRepository.createMeal(meal)

                if (isCreated) {
                    withContext(Dispatchers.Main) {
                        _lastCreatedMeal.postValue(meal)
                        _uiHandler.setSuccess()
                        _mealCreationSuccess.postValue(true)
                        _addedIngredientsState.value = emptyList()
                        _mealName.value = ""
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiHandler.setErrorState(DatabaseError())
                        _mealCreationSuccess.postValue(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _uiHandler.setErrorState(DatabaseError())
                    _mealCreationSuccess.postValue(false)
                }
            }
        }
    }

    fun consumeMeal(meal: Meal){
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }

            try {
                val isConsumed = dietsPreviewRepository.consumeMeal(meal)
                if (isConsumed) {
                    withContext(Dispatchers.Main) {
                        mealsRepository.incrementMealIndex()
                        _uiHandler.setSuccess()
                        _consumeMealState.value = true
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiHandler.setErrorState(DatabaseError())
                        _consumeMealState.value = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _uiHandler.setErrorState(DatabaseError())
                    _consumeMealState.value = false
                }
            }
        }
    }
}