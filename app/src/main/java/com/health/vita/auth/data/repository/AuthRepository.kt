package com.health.vita.auth.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.health.vita.auth.data.data_source.AuthService
import com.health.vita.auth.data.data_source.AuthServiceImpl
import com.health.vita.domain.model.User

interface AuthRepository {
    suspend fun signin(email:String, password:String)
    fun singout()
    //suspend fun updateEmail(newEmail: String, password: String)
}

class AuthRepositoryImpl (
    val authService: AuthService = AuthServiceImpl()
) : AuthRepository {

    override suspend fun signin(email: String, password: String) {
        authService.loginWithEmailAndPassword(email, password)
    }


    override fun singout() {
        authService.logout()
    }

    /*override suspend fun updateEmail(newEmail: String, password: String) {
        authService.updateEmail(newEmail, password )
    }*/


}