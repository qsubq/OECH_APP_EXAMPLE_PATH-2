package com.example.oech_app_new.domain.useCase

import com.example.oech_app_new.data.model.UserModel
import com.example.oech_app_new.data.remoteDataSource.repository.RemoteRepositoryImpl
import com.example.oech_app_new.domain.repository.RemoteRepository

class GetUserInfoUseCase() {
    private val repository: RemoteRepository = RemoteRepositoryImpl()

    suspend fun execute(email: String): List<UserModel> {
        return repository.getUserInfo(email)
    }
}
