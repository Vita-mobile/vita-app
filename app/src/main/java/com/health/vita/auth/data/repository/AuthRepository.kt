package com.health.vita.auth.data.repository

import com.health.vita.auth.data.data_source.AuthService
import com.health.vita.auth.data.data_source.AuthServiceImpl
import com.health.vita.domain.model.User

interface AuthRepository {
    suspend fun signin(email:String, password:String)
    fun singout()
    suspend fun updateEmail(newEmail: String)
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

    override suspend fun updateEmail(newEmail: String) {
        authService.updateEmail(newEmail)
    }


}