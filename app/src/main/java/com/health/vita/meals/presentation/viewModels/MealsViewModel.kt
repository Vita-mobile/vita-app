import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.meals.presentation.data.repository.MealsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MealsViewModel(private val repository: MealsRepository) : ViewModel() {

    private val _lastRecordedMeal = MutableLiveData<Int>()
    val lastRecordedMeal: LiveData<Int> get() = _lastRecordedMeal

    init {
        viewModelScope.launch {
            repository.getLastMealIndex().collect { index ->
                _lastRecordedMeal.value = index
            }
        }
    }

    fun incrementLastRecordedMeal() {
        viewModelScope.launch {
            repository.incrementMealIndex()
            repository.getLastMealIndex().collect { index ->
                _lastRecordedMeal.value = index
            }
        }
    }
}
