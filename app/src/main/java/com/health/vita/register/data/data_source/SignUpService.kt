package com.health.vita.register.data.data_source


import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage

import com.health.vita.domain.model.User
import kotlinx.coroutines.tasks.await

interface AuthService {

    suspend fun createUser(email: String,password: String)
    suspend fun isRepeatedEmail(email: String): Boolean

}

class AuthServiceImpl: AuthService {


    override suspend fun createUser(email: String,password: String){
        Firebase.auth.createUserWithEmailAndPassword(email,password).await()
    }

    override suspend fun isRepeatedEmail(email: String): Boolean {
        val result = Firebase.firestore.collection("User")
            .whereEqualTo("email", email)
            .get()
            .await()

        return !result.isEmpty
    }

}

