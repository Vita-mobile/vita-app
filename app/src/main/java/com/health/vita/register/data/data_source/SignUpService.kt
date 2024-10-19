package com.health.vita.register.data.data_source


import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

interface AuthService {

    suspend fun createUser(email: String,password: String)
}

class AuthServiceImpl: AuthService {

    override suspend fun createUser(email: String,password: String){
        Firebase.auth.createUserWithEmailAndPassword(email,password).await()
    }

}