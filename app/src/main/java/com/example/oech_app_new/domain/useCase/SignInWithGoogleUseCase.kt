package com.example.oech_app_new.domain.useCase

import com.example.oech_app_new.data.remoteDataSource.repository.RemoteRepositoryImpl
import com.example.oech_app_new.domain.repository.RemoteRepository

class SignInWithGoogleUseCase  {
    private val repository: RemoteRepository = RemoteRepositoryImpl()

    suspend fun execute() {
        repository.signInWithGoogle()
    }
}