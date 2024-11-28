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

class CreationsDietsPreviewViewModel(
    context: Context,
    private val dietsPreviewRepository: DietsPreviewRepository = DietsPreviewRepositoryImpl(),
    private val mealsRepository: MealsRepository = MealsRepositoryImpl(context)
) : ViewModel() {

    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _favorites = MutableLiveData<List<Meal>>(emptyList())
    val favorites: LiveData<List<Meal>> get() = _favorites

    private val _creations = MutableLiveData<List<Meal>>(emptyList())
    val creations: LiveData<List<Meal>> get() = _creations

    private val _consumeMealState = MutableLiveData<Boolean>()
    val consumeMealState: LiveData<Boolean> = _consumeMealState

    fun loadCreations() {
        viewModelScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
            }

            try {
                val creationsList: List<Meal> = dietsPreviewRepository.getCreations()
                _creations.postValue(creationsList)

                val favoritesList: List<Meal> = dietsPreviewRepository.getFavorites()
                _favorites.postValue(favoritesList)

                withContext(Dispatchers.Main) {
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
