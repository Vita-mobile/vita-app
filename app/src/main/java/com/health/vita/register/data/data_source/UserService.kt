package com.health.vita.register.data.data_source


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.health.vita.domain.model.User
import kotlinx.coroutines.tasks.await


interface UserService {

   suspend fun createUser(user: User)

}

class UserServiceImpl: UserService {

    override suspend fun createUser(user: User) {
        Firebase.firestore.collection("User").document(user.id).set(user).await()
    }

}