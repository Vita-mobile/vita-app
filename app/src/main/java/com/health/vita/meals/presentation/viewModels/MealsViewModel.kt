import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.meals.data.repository.MealsRepository
import com.health.vita.meals.data.repository.MealsRepositoryImpl
import kotlinx.coroutines.launch

class MealsViewModel(context: Context, private val repository: MealsRepository = MealsRepositoryImpl(context)) : ViewModel() {

    private val _lastRecordedMeal = MutableLiveData<Int>()
    val lastRecordedMeal: LiveData<Int> get() = _lastRecordedMeal
    private val _mealCount = MutableLiveData<Int>()
    val mealCounts: LiveData<Int> get() = _mealCount

    init {
        viewModelScope.launch {
            _mealCount.value = repository.getMealsCount(Firebase.auth.currentUser?.uid?:"")
        }
    }

    fun getCurrentMeal() {
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
