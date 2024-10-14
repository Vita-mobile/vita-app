package com.health.vita.auth.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.health.vita.domain.model.User
import com.health.vita.repository.AuthRepository
import com.health.vita.repository.AuthRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupViewModel(
    val repo: AuthRepository = AuthRepositoryImpl()
): ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name;

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password;

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email;

    private val _photoUri = MutableLiveData<String>()
    val photoUri: LiveData<String> get() = _photoUri;

    private val _age = MutableLiveData<Int>()
    val age: LiveData<Int> get() = _age;

    private val _weight = MutableLiveData<Float>()
    val weight: LiveData<Float> get() = _weight;

    private val _height = MutableLiveData<Float>()
    val height: LiveData<Float> get() = _height;

    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> get() = _gender;

    private val _activityLevel = MutableLiveData<String>()
    val activityLevel: LiveData<String> get() = _activityLevel;

    private val _goal = MutableLiveData<String>()
    val goal: LiveData<String> get() = _goal;

    fun setName(name: String) {
        _name.value = name
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPhotoUri(uri: String) {
        _photoUri.value = uri
    }

    fun setActivityLevel(activityLevel: String) {
        _activityLevel.value = activityLevel
    }

    fun setWeight(weight: Float) {
        _weight.value = weight
    }

    fun setHeight(height: Float) {
        _height.value = height
    }

    fun setGoal(goal: String) {
        _goal.value = goal
    }

    fun setAge(age: Int) {
        _age.value = age
    }

    fun setGender(gender: String) {
        _gender.value = gender
    }

    fun getValue(data: MutableLiveData<String>): String {
        return data.value ?: ""
    }
    fun getValue(data: MutableLiveData<Float>): Float {
        return data.value ?: 0.0f
    }

    fun getValue(data: MutableLiveData<Int>): Int {
        return data.value ?: 0
    }


        // Método para enviar los datos al repositorio al final
    fun completeSignup() {
        val user = User("", getValue(_name), getValue(_email), getValue(_photoUri), getValue(_weight), getValue(_age), getValue(_height), getValue(_gender), getValue(_goal), getValue(_activityLevel))
        viewModelScope.launch(Dispatchers.IO) {
            repo.signup(user, getValue(_password))
        }
    }


}