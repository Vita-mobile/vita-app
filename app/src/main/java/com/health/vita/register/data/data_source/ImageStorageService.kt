package com.health.vita.register.data.data_source

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface ImageStorageService {

    suspend fun uploadProfileImage(uri: Uri, id: String)
    suspend fun getDefaultImageUris(): List<String>
    suspend fun saveDefaultImage(imageId: String)
}

class ImageStorageServiceImpl : ImageStorageService {


    //get the default preloaded images
    override suspend fun getDefaultImageUris(): List<String> {

        val defaultFolder = Firebase.storage.reference.child("default")

        val result = defaultFolder.listAll().await()

        val output = result.items.map { storageReference ->
            storageReference.path
        }

        return output
    }

    override suspend fun saveDefaultImage(imageId: String) {

        Firebase.storage.reference.child("default").child(imageId)

    }

    override suspend fun uploadProfileImage(uri: Uri, imageId: String) {

        Firebase.storage.reference.child("profile_images").child(imageId).putFile(uri).await()

    }
}