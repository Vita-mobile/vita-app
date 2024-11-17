package com.health.vita.profile.data.data_source

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.health.vita.domain.model.User
import kotlinx.coroutines.tasks.await


interface UserService {
    suspend fun getUserById(id:String): User?
    suspend fun updateUserData(user: User):User?
}

class UserServiceImpl:UserService{

    override suspend fun getUserById(id: String): User? {
        val user = Firebase.firestore
            .collection("User")
            .document(id)
            .get()
            .await()
        val userObject = user.toObject(User::class.java)
        return userObject
    }

    override suspend fun updateUserData(user: User): User? {

        val  updateUser = Firebase.firestore.collection("User").document(user.id).set(user).await()
        return getUserById(user.id)
    }





}