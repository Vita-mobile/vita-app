package com.health.vita.auth.data.data_source

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.health.vita.domain.model.User
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait


interface AuthService {
    suspend fun loginWithEmailAndPassword(email: String, password: String)
    fun logout()

    //suspend fun updateEmail(newEmail:String, password: String)

}

class AuthServiceImpl : AuthService {

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }


    override fun logout() {
        Firebase.auth.signOut()
    }


    /*override suspend fun updateEmail(newEmail: String, password: String) {

        val currentUser = Firebase.auth.currentUser
        val cred = EmailAuthProvider.getCredential(currentUser?.email?:"", password)

        if (currentUser != null) {
            try {

                 currentUser.reauthenticate(cred).await()
                 currentUser.verifyBeforeUpdateEmail(newEmail)

            } catch (e: Exception) {
                Log.e("AuthServiceImpl", "Error al actualizar el correo", e)
                throw e
            }
        } else {
            Log.e("AuthServiceImpl", "No hay un usuario autenticado")
            throw IllegalStateException("No hay un usuario autenticado")
        }


    }*/
}