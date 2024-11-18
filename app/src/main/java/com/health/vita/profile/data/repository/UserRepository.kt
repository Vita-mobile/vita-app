package com.health.vita.profile.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.domain.model.User
import com.health.vita.profile.data.data_source.UserService
import com.health.vita.profile.data.data_source.UserServiceImpl


interface UserRepository {
    suspend fun getCurrentUser():User?

    suspend fun updateUserData(user: User):User?
}

class UserRepositoryImpl(
    val userService: UserService = UserServiceImpl()
):UserRepository{
    override suspend fun getCurrentUser(): User? {
        Firebase.auth.currentUser?.let {
            return userService.getUserById(it.uid)
        } ?: run {
            return null
        }
    }

    override suspend fun updateUserData(user: User): User? {



        return userService.updateUserData(user)
    }


}