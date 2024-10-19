package com.health.vita.auth.data.data_source

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


interface AuthService {
    fun loginWithEmailAndPassword(email: String, password: String)
    fun logout()
}
class AuthServiceImpl: AuthService {
    override fun loginWithEmailAndPassword(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
    }

    override fun logout() {
        TODO("Not yet implemented")
    }


}