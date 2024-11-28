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
import com.health.vita.meals.data.repository.MealsRepository
import com.health.vita.meals.data.repository.MealsRepositoryImpl
import com.health.vita.meals.domain.model.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IADietsPreviewViewModel(
    private val dietsPreviewRepository: DietsPreviewRepository = DietsPreviewRepositoryImpl(),
    private val mealsRepository: MealsRepository = MealsRepositoryImpl()
) : ViewModel() {

    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _mealsIA = MutableLiveData<List<Meal>>(emptyList())
    val mealsIA: LiveData<List<Meal>> get() = _mealsIA

    private val _favorites = MutableLiveData<List<Meal>>(emptyList())
    val favorites: LiveData<List<Meal>> get() = _favorites

    private val _consumeMealState = MutableLiveData<Boolean>()
    val consumeMealState: LiveData<Boolean> = _consumeMealState

    fun loadOrGenerateMealsIA(meal: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }

                try {
                    val mealsList: List<Meal> = dietsPreviewRepository.getMealsIA(meal)
                    val favoritesList: List<Meal> = dietsPreviewRepository.getFavorites()
                    _favorites.postValue(favoritesList)

                    if (mealsList.isEmpty()) {
                        val mealsQuantity: Int = mealsRepository.getMealsCount()
                        val isGenerated = dietsPreviewRepository.generateMealsIA(mealsQuantity)
                        if (isGenerated) {
                            val newMeals = dietsPreviewRepository.getMealsIA(meal)
                            _mealsIA.postValue(newMeals)
                            withContext(Dispatchers.Main) {
                                _uiHandler.setSuccess()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                _uiHandler.setErrorState(DatabaseError())
                            }
                        }
                    } else {
                        _mealsIA.postValue(mealsList)
                        withContext(Dispatchers.Main) {
                            _uiHandler.setSuccess()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        _uiHandler.setErrorState(DatabaseError())
                    }
                }
        }
    }

    fun rechargeMealsIA(meal: Int){
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }

            try {
                val isRecharged = dietsPreviewRepository.rechargeMealIA(meal)
                if (isRecharged) {
                    val newMeals = dietsPreviewRepository.getMealsIA(meal)
                    _mealsIA.postValue(newMeals)
                    withContext(Dispatchers.Main) {
                        _uiHandler.setSuccess()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiHandler.setErrorState(DatabaseError())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _uiHandler.setErrorState(DatabaseError())
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
