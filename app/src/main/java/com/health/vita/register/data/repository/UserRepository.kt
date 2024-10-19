package com.health.vita.register.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.domain.model.User
import com.health.vita.register.data.data_source.UserService
import com.health.vita.register.data.data_source.UserServiceImpl

interface UserRepository {

    suspend fun createUser(user: User)

}

class UserRepositoryImpl(

    private val userService: UserService = UserServiceImpl()

):UserRepository{

    override suspend fun createUser(user: User) {
        userService.createUser(user)
    }


}