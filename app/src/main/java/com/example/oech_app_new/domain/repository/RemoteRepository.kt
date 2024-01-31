package com.example.oech_app_new.domain.repository

import com.example.oech_app_new.data.model.UserModel

interface RemoteRepository {
    suspend fun signUp(em: String, pas: String, number: Int, fullName: String)
    suspend fun signIn(em: String, pas: String)
    suspend fun sendOTPCode(em: String)
    suspend fun otpVerification(em: String, code: String)
    suspend fun resendEmailCode(email: String)
    suspend fun setNewPassword(pass: String, email: String): List<UserModel>
    suspend fun signInWithGoogle()
    suspend fun createNewUser(em: String, pas: String, number: Int, fullName: String)
    suspend fun getUserInfo(email: String): List<UserModel>
}
