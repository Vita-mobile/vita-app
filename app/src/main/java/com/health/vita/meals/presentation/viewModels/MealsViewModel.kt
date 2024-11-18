import android.content.Context
import android.util.Log
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
    init {
        viewModelScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
                getLastEatenMeal()
                getCurrentMeal()
                _mealCount.postValue(repository.getMealsCount())
                _uiHandler.setSuccess()
            }
        }
    }

    fun getCurrentMeal() {
        viewModelScope.launch(Dispatchers.IO){
            repository.getLastMealIndex().collect { index ->
                _lastRecordedMeal.postValue(index)
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
            Log.e(">>> Ultima", lastEatenDate.get(Calendar.DAY_OF_YEAR).toString())
            Log.e(">>> Hoy", today.get(Calendar.DAY_OF_YEAR).toString())

            val lastEaten = lastEatenDate.get(Calendar.YEAR) >= today.get(Calendar.YEAR) &&
                    lastEatenDate.get(Calendar.DAY_OF_YEAR) >= today.get(Calendar.DAY_OF_YEAR)
            _lastEatenMeal.postValue(lastEaten)
            if (!lastEaten) {
                Log.e(">>>", "Fue reseteado")
                resetMealIndex()
            }
        }
    }

    private fun resetMealIndex() {
        viewModelScope.launch(Dispatchers.IO){
            repository.resetMealIndex()
            repository.getLastMealIndex().collect {
                index ->
                    _lastRecordedMeal.postValue(index)
            }
        }
    }
}
