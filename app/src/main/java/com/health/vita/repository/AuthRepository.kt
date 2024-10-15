package com.health.vita.repository

import com.health.vita.domain.model.User
import com.health.vita.service.AuthService


interface AuthRepository {
    suspend fun signup(user: User, password:String)
    suspend fun signin(email:String, password:String)
}
class AuthRepositoryImpl(
    val authService: AuthService = AuthService()/*=  AuthServiceImpl()*/,
    val userRepository: UserRepository = UserRepository() //= UserRepositoryImpl()
) : AuthRepository{
    override suspend fun signup(user: User, password: String) {
        // TODO
    }

    override suspend fun signin(email: String, password: String) {
        // TODO
    }
}
