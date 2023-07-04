package com.example.oech_app_new.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val email: String,
    val password: String,
    val number: Int,
    val full_name: String,
)
