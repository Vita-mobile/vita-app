package com.health.vita.register.presentation.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.health.vita.domain.model.User
import com.health.vita.register.presentation.service.UserServices
import com.health.vita.register.presentation.service.UserServicesImpl


interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getCurrentUser(): User?
}

class UserRepositoryImpl(

    val userServices: UserServices = UserServicesImpl()

):UserRepository{

    override suspend fun createUser(user: User) {
        userServices.createUser(user)
    }

    override suspend fun getCurrentUser(): User? {

        Firebase.auth.currentUser?.let{

            return userServices.getUserById(it.uid)

        } ?: run {

            return null

        }


    }

}