package com.health.vita.auth.data.repository

import com.health.vita.auth.data.data_source.AuthService
import com.health.vita.auth.data.data_source.AuthServiceImpl
import com.health.vita.domain.model.User

interface AuthRepository {
    suspend fun signup(user: User, password:String)
    suspend fun signin(email:String, password:String)
}

class AuthRepositoryImpl (
    val authService: AuthService = AuthServiceImpl()
) : AuthRepository {
    override suspend fun signup(user: User, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun signin(email: String, password: String) {
        authService.loginWithEmailAndPassword(email, password)
    }


}