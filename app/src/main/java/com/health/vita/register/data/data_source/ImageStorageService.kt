package com.health.vita.register.data.data_source

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

interface ImageStorageService{

    suspend fun uploadProfileImage(userId: String, imageUri: Uri, isDefault: Boolean): String
    suspend fun getDefaultImageUris(): List<String>
}

class ImageStorageServiceImpl: ImageStorageService{

    //get the default preloaded images
    override suspend fun getDefaultImageUris(): List<String> {

        val defaultFolder = Firebase.storage.reference.child("default")
        val defaultImages = mutableListOf<String>()

        val result = defaultFolder.listAll().await()

        result.items.forEach { item ->
            val url = item.downloadUrl.await().toString()
            defaultImages.add(url)
        }

        Log.d("ESTA_ENTRANDO", "Imágenes predeterminadas cargadas: $defaultImages")

        return defaultImages
    }

    override suspend fun uploadProfileImage(
        userId: String,
        imageUri: Uri,
        isDefault: Boolean
    ): String {
        TODO("Not yet implemented")
    }
}