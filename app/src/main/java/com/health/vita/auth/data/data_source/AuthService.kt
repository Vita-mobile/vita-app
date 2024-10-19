package com.health.vita.auth.data.data_source

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


interface AuthService {
    suspend fun loginWithEmailAndPassword(email: String, password: String)
    fun logout()
}

class AuthServiceImpl : AuthService {
    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }




    override fun logout() {
        TODO("Not yet implemented")
    }


}