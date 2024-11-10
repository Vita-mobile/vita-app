import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.error_management.DatabaseError
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.repository.DietsPreviewRepository
import com.health.vita.meals.data.repository.DietsPreviewRepositoryImpl
import com.health.vita.meals.domain.model.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DietsPreviewViewModel(
    private val dietsPreviewRepository: DietsPreviewRepository = DietsPreviewRepositoryImpl()
) : ViewModel() {

    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _mealsIA = MutableLiveData<List<Meal>>(emptyList())
    val mealsIA: LiveData<List<Meal>> get() = _mealsIA

    fun loadOrGenerateMealsIA() {
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }

                try {
                    val mealsList: List<Meal> = dietsPreviewRepository.getMealsIA()

                    if (mealsList.isEmpty()) {
                        val isGenerated = dietsPreviewRepository.generateMealsIA()
                        if (isGenerated) {
                            val newMeals = dietsPreviewRepository.getMealsIA()
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
}
