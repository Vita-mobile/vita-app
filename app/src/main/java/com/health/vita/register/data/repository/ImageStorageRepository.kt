package com.health.vita.register.data.repository


import android.net.Uri

import com.health.vita.register.data.data_source.ImageStorageService
import com.health.vita.register.data.data_source.ImageStorageServiceImpl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ProfileImageRepository {

    suspend fun uploadUserProfileImage(userId: String, imageUri: Uri, isDefault: Boolean): String
    suspend fun getDefaultProfileImages(): List<String>
}

class ProfileImageRepositoryImpl(
    private val imageStorageService: ImageStorageService =  ImageStorageServiceImpl()
) : ProfileImageRepository {

    //logic no yet implemented
    override suspend fun uploadUserProfileImage(userId: String, imageUri: Uri, isDefault: Boolean): String {

        return withContext(Dispatchers.IO) {
            imageStorageService.uploadProfileImage(userId, imageUri, isDefault)
        }
    }

    //Main functionality to be resolved
    override suspend fun getDefaultProfileImages(): List<String> {

        return withContext(Dispatchers.IO) {
            imageStorageService.getDefaultImageUris()
        }
    }
}
