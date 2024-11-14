package com.health.vita.register.data.repository


import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.health.vita.domain.model.User

import com.health.vita.register.data.data_source.ImageStorageService
import com.health.vita.register.data.data_source.ImageStorageServiceImpl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

interface ProfileImageRepository {

    suspend fun uploadUserProfileImage(uri: Uri?, isDefault: Boolean)
    suspend fun getDefaultProfileImages(): List<String>

}

class ProfileImageRepositoryImpl(
    private val imageStorageService: ImageStorageService =  ImageStorageServiceImpl()
) : ProfileImageRepository {


    override suspend fun uploadUserProfileImage(//user: User,
        uri: Uri?, isDefault: Boolean){

        val imageID: String
        //val user : User

        uri?.let {

            if (isDefault) {

                imageID = uri.lastPathSegment ?: ""

                imageStorageService.saveDefaultImage(imageID)
            } else {

                imageID = UUID.randomUUID().toString()

                imageStorageService.uploadProfileImage(it, imageID)
            }
        }

    }
    
    override suspend fun getDefaultProfileImages(): List<String> {

        val defaultPaths = imageStorageService.getDefaultImageUris()

        val imageUrls = defaultPaths.map { path ->
            val storageReference = Firebase.storage.getReference(path)
            storageReference.downloadUrl.await().toString()
        }

        return imageUrls
    }
}
