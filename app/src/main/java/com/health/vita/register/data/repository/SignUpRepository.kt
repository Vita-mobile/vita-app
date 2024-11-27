package com.health.vita.register.data.repository


import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.health.vita.domain.model.User
import com.health.vita.register.data.data_source.AuthService
import com.health.vita.register.data.data_source.AuthServiceImpl
import java.util.UUID


interface SignUpRepository {

    suspend fun signup(user: User, password: String)


    suspend fun isRepeatedEmail(email: String): Boolean

}


class SignUpRepositoryImpl(

    private val authService: AuthService = AuthServiceImpl(),
    private val userRepository: UserRepository = UserRepositoryImpl()

    ): SignUpRepository {

    override suspend fun signup(user: User, password: String) {

        //1. Sign up on firebase auth module
        authService.createUser(user.email,password)
        //2. Get UID from firebase auth module
        val uid = Firebase.auth.currentUser?.uid
        //3. Create user on Firestore
        Log.e("SignUpRepositoryImpl", "uid: $uid")
        uid?.let {
            user.id = it
            userRepository.createUser(user)

        }?: throw Exception("UID is null")

    }

    override suspend fun isRepeatedEmail(email: String): Boolean {
        return authService.isRepeatedEmail(email)
    }

    /**override suspend fun saveImage(uri: Uri?) {

        uri?.let {
            val imageID = UUID.randomUUID().toString()
            user.imageID = imageID
            chatService.uploadImage(it, imageID)
        }
    }**/




}