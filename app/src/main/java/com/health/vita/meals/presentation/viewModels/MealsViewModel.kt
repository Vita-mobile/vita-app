import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.meals.data.repository.MealTrackingRepositoryImpl
import com.health.vita.meals.data.repository.MealsRepository
import com.health.vita.meals.data.repository.MealsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MealsViewModel(context: Context, private val repository: MealsRepository = MealsRepositoryImpl(context)) : ViewModel() {
    private val _uiHandler = UiHandler()
    val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _lastRecordedMeal = MutableLiveData<Int>()
    val lastRecordedMeal: LiveData<Int> get() = _lastRecordedMeal
    private val _mealCount = MutableLiveData<Int>()
    val mealCounts: LiveData<Int> get() = _mealCount
    private val _lastEatenMeal = MutableLiveData<Boolean>()
    val lastEatenMeal: LiveData<Boolean> get() = _lastEatenMeal
    init {
        viewModelScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
                _mealCount.value = repository.getMealsCount()
                _uiHandler.setSuccess()
            }
        }
    }

    fun getCurrentMeal() {
        viewModelScope.launch(Dispatchers.IO){
            repository.getLastMealIndex().collect { index ->
                withContext(Dispatchers.Main){
                    _lastRecordedMeal.value = index
                }
            }
        }
    }

    fun incrementLastRecordedMeal() {
        viewModelScope.launch(Dispatchers.IO){
            repository.incrementMealIndex()
            repository.getLastMealIndex().collect { index ->
                withContext(Dispatchers.Main) {
                    _lastRecordedMeal.value = index
                }
            }
        }
    }


    fun getLastEatenMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            val date = repository.getLastEatenMealDate()
            val today = Calendar.getInstance()

            val lastEatenDate = Calendar.getInstance().apply {
                time = date.toDate()
            }

            if (lastEatenDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)
                || lastEatenDate.get(Calendar.DAY_OF_YEAR) < today.get(Calendar.DAY_OF_YEAR)) {
                withContext(Dispatchers.Main) {
                    _lastEatenMeal.value = false
                }
            } else {
                withContext(Dispatchers.Main) {
                    _lastEatenMeal.value = true
                }
            }
        }
    }


    fun resetMealIndex() {
        viewModelScope.launch(Dispatchers.IO){
            repository.resetMealIndex()
            repository.getLastMealIndex().collect {
                index ->
                withContext(Dispatchers.Main){
                    _lastRecordedMeal.value = index
                }
            }
        }
    }
}
