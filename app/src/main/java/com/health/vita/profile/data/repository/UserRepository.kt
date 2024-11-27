package com.health.vita.profile.data.repository

import android.util.Log
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
            Log.e("UserRepositoryImpl", "Current user UID: ${it.uid}")
            return userService.getUserById(it.uid)
        } ?: run {
            Log.e("UserRepositoryImpl", "Current user is null")
            return null
        }
    }

    override suspend fun updateUserData(user: User): User? {



        return userService.updateUserData(user)
    }


}