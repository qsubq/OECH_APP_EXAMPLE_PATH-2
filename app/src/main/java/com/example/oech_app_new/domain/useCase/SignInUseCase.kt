package com.example.oech_app_new.domain.useCase

import com.example.oech_app_new.data.remoteDataSource.repository.RemoteRepositoryImpl
import com.example.oech_app_new.domain.repository.RemoteRepository

class SignInUseCase {
    private val repository: RemoteRepository = RemoteRepositoryImpl()

    suspend fun execute(email: String, password: String) {
        repository.signIn(email, password)
    }
}
