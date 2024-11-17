package com.health.vita.register.data.repository


import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

import com.health.vita.register.data.data_source.ImageStorageService
import com.health.vita.register.data.data_source.ImageStorageServiceImpl

import kotlinx.coroutines.tasks.await
import java.util.UUID

interface ProfileImageRepository {

    suspend fun uploadUserProfileImage(uri: Uri?, isDefault: Boolean): String
    suspend fun getDefaultProfileImages(): List<String>
    suspend fun updateUserProfileImageID(imageId: String?)

}

class ProfileImageRepositoryImpl(
    private val imageStorageService: ImageStorageService =  ImageStorageServiceImpl()
) : ProfileImageRepository {


    override suspend fun uploadUserProfileImage(//user: User,
        uri: Uri?, isDefault: Boolean): String{

        var imageId = ""

        uri?.let {

            Log.e("ProfileImageRepository", "Uploading image with URI: $it and is default: $isDefault")
            if (isDefault) {
                imageId = uri.lastPathSegment ?: ""
                imageStorageService.saveDefaultImage(imageId)
            } else {
                imageId = UUID.randomUUID().toString()
                imageStorageService.uploadProfileImage(it, imageId)
            }
        }

         return imageId

    }

    
    override suspend fun getDefaultProfileImages(): List<String> {

        val defaultPaths = imageStorageService.getDefaultImageUris()

        val imageUrls = defaultPaths.map { path ->
            val storageReference = Firebase.storage.getReference(path)
            storageReference.downloadUrl.await().toString()
        }

        return imageUrls
    }

    override suspend fun updateUserProfileImageID(imageId: String?) {

        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {

            imageStorageService.updateUserImageId(currentUser.uid, imageId)

        } else {

            Log.e("ProfileImageRepository", "Usuario no autenticado")

        }
    }
}
