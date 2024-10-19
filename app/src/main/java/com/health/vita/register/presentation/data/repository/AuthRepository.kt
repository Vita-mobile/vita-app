package com.health.vita.register.presentation.data.repository


import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.domain.model.User
import com.health.vita.register.presentation.data.repository.service.AuthService
import com.health.vita.register.presentation.data.repository.service.AuthServiceImpl


interface AuthRepository {

    suspend fun signup(user: User, password: String)

}

class AuthRepositoryImpl(

    val authService: AuthService = AuthServiceImpl(),
    val userRepository: UserRepository = UserRepositoryImpl()

): AuthRepository {

    override suspend fun signup(user: User, password: String) {

        authService.createUser(user.email,password)

        val uid = Firebase.auth.currentUser?.uid

        uid?.let {

            user.id = it
            userRepository.createUser(user)

        }

    }


}