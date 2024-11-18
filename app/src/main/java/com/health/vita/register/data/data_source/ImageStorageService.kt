package com.health.vita.register.data.data_source

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

interface ImageStorageService {

    suspend fun uploadProfileImage(uri: Uri, id: String)
    suspend fun getDefaultImageUris(): List<String>
    suspend fun saveDefaultImage(imageId: String)
    suspend fun updateUserImageId(userId:String, imageId: String?)
    suspend fun getUserImage(imageId:String,isDefault:Boolean): String
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

    override suspend fun updateUserImageId(userId:String, imageId: String?) {

        Firebase.firestore.collection("User").document(userId).update("imageID", imageId).await()
    }

    override suspend fun getUserImage(imageId: String, isDefault:Boolean): String {


        if (isDefault) {
            return Firebase.storage.reference.child(imageId).downloadUrl.await().toString()
        } else {
            return Firebase.storage.reference.child("profile_images").child(imageId).downloadUrl.await().toString()
        }
    }
}