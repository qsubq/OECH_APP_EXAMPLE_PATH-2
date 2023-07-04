package com.example.oech_app_new.data.remoteDataSource.repository

import android.util.Log
import com.example.oech_app_new.data.model.UserModel
import com.example.oech_app_new.domain.repository.RemoteRepository
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class RemoteRepositoryImpl : RemoteRepository {
    val client = createSupabaseClient(
        supabaseUrl = "https://idpdrbamhlvemznetkfy.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlkcGRyYmFtaGx2ZW16bmV0a2Z5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE2ODgzMjM0OTcsImV4cCI6MjAwMzg5OTQ5N30.xz6C7srJJOouCRTLjd5tDr0sVyZs4U36OQju4wLGuf0",
    ) {
        install(GoTrue)
        install(Postgrest)
    }

    override suspend fun signUp(em: String, pas: String, number: Int, fullName: String) {
        val user = client.gotrue.signUpWith(Email) {
            email = em
            password = pas
        }

        createNewUser(em, pas, number, fullName)
    }

    override suspend fun signIn(em: String, pas: String) {
        client.gotrue.loginWith(Email) {
            email = em
            password = pas
        }
    }

    override suspend fun sendOTPCode(em: String) {
        client.gotrue.sendOtpTo(Email) {
            email = em
        }
    }

    override suspend fun otpVerification(em: String, code: String) {
        Log.e("Maksim", "OtpVerification")


        client.gotrue.verifyEmailOtp(type = OtpType.Email.MAGIC_LINK, email = em, token = code)
    }

    override suspend fun resendEmailCode(email: String) {
        Log.e("Maksim", "Resend Email")
        client.gotrue.resendEmail(OtpType.Email.SIGNUP, email)
    }

    override suspend fun setNewPassword(pass: String, email: String): List<UserModel> {
        val user = client.gotrue.modifyUser {
            password = pass
        }
        return getUserInfo(email)
    }

    override suspend fun signInWithGoogle() {
        client.gotrue.loginWith(Google)
    }

    override suspend fun createNewUser(em: String, pas: String, number: Int, fullName: String) {
        val user = UserModel(em, pas, number, fullName)
        client.postgrest["User"].insert(
            user,
        )
    }

    override suspend fun getUserInfo(email: String): List<UserModel> {
        return client.postgrest["User"].select(columns = Columns.list("email, password, number, full_name")) {
            UserModel::email eq email
        }.decodeAs()
    }
}
