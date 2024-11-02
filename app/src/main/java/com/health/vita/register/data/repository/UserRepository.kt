package com.health.vita.register.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.domain.model.User
import com.health.vita.register.data.data_source.UserService
import com.health.vita.register.data.data_source.UserServiceImpl

interface UserRepository {
    suspend fun createUser(user: User)
    suspend fun getCurrentUserID():String?

}

class UserRepositoryImpl(

    private val userService: UserService = UserServiceImpl()

):UserRepository{

    override suspend fun createUser(user: User) {
        userService.createUser(user)
    }

    override suspend fun getCurrentUserID(): String? {
        Firebase.auth.currentUser?.let {
            return it.uid
        } ?: run {
            return null
        }
    }


}