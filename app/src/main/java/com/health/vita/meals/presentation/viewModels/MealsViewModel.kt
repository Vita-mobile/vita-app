import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.core.utils.states_management.UiHandler
import com.health.vita.core.utils.states_management.UiState
import com.health.vita.domain.model.User
import com.health.vita.meals.data.repository.MealsRepository
import com.health.vita.meals.data.repository.MealsRepositoryImpl
import com.health.vita.meals.data.repository.NutritionalPlanRepository
import com.health.vita.meals.data.repository.NutritionalPlanRepositoryImpl
import com.health.vita.profile.data.repository.UserRepository
import com.health.vita.profile.data.repository.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class MealsViewModel(context: Context,
                     private val mealsRepository: MealsRepository = MealsRepositoryImpl(context),
                     private val userRepository: UserRepository = UserRepositoryImpl(),
                     private val nutritionalPlanRepository: NutritionalPlanRepository = NutritionalPlanRepositoryImpl(),
) : ViewModel() {
    private val _uiHandler = UiHandler()
        val uiState: LiveData<UiState> get() = _uiHandler.uiState

    private val _user = MutableLiveData<User?>(User())
    val user: LiveData<User?> get() = _user

    private val _kcal = MutableLiveData(0)
    val kcal: LiveData<Int> get() = _kcal
    private val _lastRecordedMeal = MutableLiveData<Int>()
    val lastRecordedMeal: LiveData<Int> get() = _lastRecordedMeal
    private val _mealCount = MutableLiveData<Int>()
    val mealCounts: LiveData<Int> get() = _mealCount
    private val _lastEatenMeal = MutableLiveData<Boolean>()


    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val me = userRepository.getCurrentUser()
            withContext(Dispatchers.Main) {
                _user.value = me
            }
        }
    }

    fun fetchMealsState(){
        viewModelScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main) {
                _uiHandler.setLoadingState()
                getLastEatenMeal()
                getCurrentMeal()
                _mealCount.value = mealsRepository.getMealsCount()
                _uiHandler.setSuccess()
            }
        }
    }

    fun getCurrentMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            mealsRepository.getLastMealIndex().collect { index ->
                withContext(Dispatchers.Main) {
                    _lastRecordedMeal.value = index
                }
            }
        }
    }

    fun getLastEatenMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            val date = mealsRepository.getLastEatenMealDate()
            val today = Calendar.getInstance()
            val lastEatenDate = Calendar.getInstance().apply {
                time = date.toDate()
            }
            val lastEaten = lastEatenDate.get(Calendar.YEAR) >= today.get(Calendar.YEAR) &&
                    lastEatenDate.get(Calendar.DAY_OF_YEAR) >= today.get(Calendar.DAY_OF_YEAR)
            withContext(Dispatchers.Main){
                _lastEatenMeal.value = lastEaten;
            }
            if (_lastEatenMeal.value == false) {
                Log.e(">>>", "Fue reseteado")
                resetMealIndex()
            }
        }
    }

    private fun resetMealIndex() {
        viewModelScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                mealsRepository.resetMealIndex()
                mealsRepository.getLastMealIndex().collect {
                        index ->
                    _lastRecordedMeal.value = index
                }
            }

        }
    }

    fun obtainDailyCalories(){

        viewModelScope.launch (Dispatchers.IO){

            val nutritionalPlan = nutritionalPlanRepository.getNutritionalPlan()

            withContext(Dispatchers.Main){
                _kcal.value = nutritionalPlan?.kcalGoal?.toInt()
            }

        }
    }
}
